# 07 - Execução com Docker Compose

## 🐳 Visão Geral do Docker Compose

Docker Compose permite executar a aplicação em containers orquestrados, criando um ambiente completo com:

- **API** (Spring Boot em Java 21)
- **MySQL Database** (MySQL 8.0)
- **Rede compartilhada** para comunicação entre containers

## 📋 Arquivo docker-compose.yml

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: mysql_gestao_restaurantes
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: gestao_restaurantes
      MYSQL_USER: app_user
      MYSQL_PASSWORD: app_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - app-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  api:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: api_gestao_restaurantes
    depends_on:
      mysql:
        condition: service_healthy
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/gestao_restaurantes?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
      SPRING_DATASOURCE_USERNAME: app_user
      SPRING_DATASOURCE_PASSWORD: app_password
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    networks:
      - app-network

volumes:
  mysql_data:

networks:
  app-network:
    driver: bridge
```

## 🔧 Dockerfile

```dockerfile
# ===== Etapa 1: build =====
FROM maven:3.9.15-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ===== Etapa 2: runtime =====
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Explicação do Dockerfile

**Etapa 1: Build**

- Usa imagem Maven com Java 21
- Copia `pom.xml` e `src/`
- Executa `mvn clean package` para compilar

**Etapa 2: Runtime**

- Usa apenas a imagem JDK (menor tamanho)
- Copia o JAR compilado da etapa 1
- Define porta 8080 como EXPOSE
- Define comando de entrada para executar o JAR

## 🚀 Passo a Passo para Executar

### Pré-requisitos

- **Docker** instalado (https://www.docker.com/products/docker-desktop)
- **Docker Compose** (geralmente vem com Docker Desktop)

### 1. Verificar Instalação

```bash
# Verificar Docker
docker --version
# Resultado esperado: Docker version X.X.X

# Verificar Docker Compose
docker-compose --version
# Resultado esperado: Docker Compose version X.X.X
```

### 2. Clonar ou Acessar o Repositório

```bash
cd /caminho/para/api-gestao-gestaurantes
```

### 3. Iniciar os Containers

```bash
# Iniciar em modo detached (background)
docker-compose up -d

# Ou iniciar em modo foreground (vê logs em tempo real)
docker-compose up
```

**Saída esperada**:

```
Creating network "api-gestao-gestaurantes_app-network" with driver "bridge"
Creating volume "api-gestao-gestaurantes_mysql_data" with default driver
Creating mysql_gestao_restaurantes ... done
Creating api_gestao_restaurantes ... done
```

### 4. Aguardar Inicialização

Espere alguns segundos para que:

1. MySQL inicie e fique pronto
2. API compile (primeira vez pode levar 2-3 minutos)
3. API se conecte ao MySQL

Verifique com:

```bash
docker-compose logs -f
```

Procure por:

```
mysql_gestao_restaurantes | Ready for connections
api_gestao_restaurantes | Started Application in X.XXX seconds
```

### 5. Verificar se os Containers estão Rodando

```bash
docker-compose ps

# Saída esperada:
# NAME                          COMMAND                  SERVICE             STATUS
# mysql_gestao_restaurantes     "docker-entrypoint.s…"   mysql               Up X minutes (healthy)
# api_gestao_restaurantes       "java -jar app.jar"      api                 Up X minutes
```

### 6. Acessar a Aplicação

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### 7. Testar Endpoints

```bash
# Criar usuário
curl -X POST http://localhost:8080/api/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "login": "joao",
    "senha": "senha123",
    "tipo": "CLIENTE",
    "endereco": "Rua Exemplo, 123"
  }'

# Listar usuários
curl http://localhost:8080/api/v1/usuarios

# Login
curl -X POST http://localhost:8080/api/v1/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao",
    "senha": "senha123"
  }'
```

## 🔐 Variáveis de Ambiente

### MySQL

| Variável                | Valor               | Descrição                 |
| ----------------------- | ------------------- | ------------------------- |
| **MYSQL_ROOT_PASSWORD** | root                | Senha do usuário root     |
| **MYSQL_DATABASE**      | gestao_restaurantes | Banco de dados criado     |
| **MYSQL_USER**          | app_user            | Usuário de aplicação      |
| **MYSQL_PASSWORD**      | app_password        | Senha do usuário app_user |

### Spring Boot (API)

| Variável                          | Valor                                       | Descrição                        |
| --------------------------------- | ------------------------------------------- | -------------------------------- |
| **SPRING_DATASOURCE_URL**         | jdbc:mysql://mysql:3306/gestao_restaurantes | URL de conexão                   |
| **SPRING_DATASOURCE_USERNAME**    | app_user                                    | Usuário do BD                    |
| **SPRING_DATASOURCE_PASSWORD**    | app_password                                | Senha do BD                      |
| **SPRING_JPA_HIBERNATE_DDL_AUTO** | update                                      | Atualizar schema automaticamente |

### Personalizar Variáveis

Para usar outras credenciais, crie um arquivo `.env`:

```bash
# .env
MYSQL_ROOT_PASSWORD=root_seguro_123
MYSQL_PASSWORD=app_password_seguro_456
SPRING_DATASOURCE_USERNAME=usuario_app
SPRING_DATASOURCE_PASSWORD=senha_app_456
```

Então use em `docker-compose.yml`:

```yaml
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD:-root}
  MYSQL_PASSWORD: ${MYSQL_PASSWORD:-app_password}
```

Execute:

```bash
docker-compose up -d
```

## ⏹️ Parar os Containers

### Parar sem remover

```bash
docker-compose stop

# Saída esperada:
# Stopping api_gestao_restaurantes ... done
# Stopping mysql_gestao_restaurantes ... done
```

### Reiniciar após parar

```bash
docker-compose start
```

### Remover containers, volumes e networks

```bash
docker-compose down

# Saída esperada:
# Stopping api_gestao_restaurantes ... done
# Stopping mysql_gestao_restaurantes ... done
# Removing api_gestao_restaurantes ... done
# Removing mysql_gestao_restaurantes ... done
# Removing network api-gestao-gestaurantes_app-network
```

### Remover também volumes (dados do MySQL)

```bash
docker-compose down -v

# ⚠️ CUIDADO: Isso deleta todos os dados do MySQL
```

## 📊 Visualizar Logs

### Logs de todos os containers

```bash
docker-compose logs
```

### Logs em tempo real

```bash
docker-compose logs -f
```

### Logs de um container específico

```bash
# Logs do MySQL
docker-compose logs mysql

# Logs da API
docker-compose logs api

# Em tempo real
docker-compose logs -f api
```

## 🔍 Acessar Container em Execução

### Shell da API

```bash
docker-compose exec api /bin/bash

# Dentro do container:
ps aux          # Ver processos
java -version   # Ver versão Java
exit            # Sair
```

### Shell do MySQL

```bash
docker-compose exec mysql bash

# Ou conectar direto ao MySQL
docker-compose exec mysql mysql -u app_user -p

# Dentro do MySQL:
mysql> SHOW DATABASES;
mysql> USE gestao_restaurantes;
mysql> SHOW TABLES;
mysql> SELECT * FROM usuario;
mysql> EXIT;
```

## 🐛 Troubleshooting

### Problema: Porta 3306 já em uso

```bash
# Listar processos usando a porta
lsof -i :3306

# Matar processo
kill -9 <PID>

# Ou alterar porta em docker-compose.yml:
# ports:
#   - "3307:3306"  (usar 3307 em vez de 3306)
```

### Problema: Porta 8080 já em uso

```bash
# Listar processos
lsof -i :8080

# Matar processo
kill -9 <PID>

# Ou alterar porta em docker-compose.yml:
# ports:
#   - "8081:8080"  (usar 8081 em vez de 8080)
```

### Problema: API não conecta ao MySQL

```bash
# Verificar saúde do MySQL
docker-compose exec mysql mysqladmin -u app_user -p ping

# Verificar logs da API
docker-compose logs api

# Procure por erros de conexão como:
# com.mysql.cj.jdbc.exceptions.CommunicationsException
```

### Problema: Erro ao compilar

```bash
# Limpar volumes e reconstruir
docker-compose down -v
docker-compose build --no-cache

# Depois executar novamente
docker-compose up -d
```

### Problema: Espaço em disco insuficiente

```bash
# Listar imagens e volumes
docker images
docker volume ls

# Remover imagens não usadas
docker image prune -a

# Remover volumes não usados
docker volume prune
```

## 🛠️ Comandos Úteis

### Reconstruir Imagem

```bash
# Reconstruir a imagem da API
docker-compose build --no-cache api

# Depois reiniciar
docker-compose up -d api
```

### Rebuild Completo

```bash
# Remover tudo e recriar do zero
docker-compose down -v
docker-compose up -d --build
```

### Remover Containers Inativos

```bash
docker container prune

# Ou com força
docker container prune -f
```

### Inspecionar Container

```bash
# Ver detalhes da API
docker inspect api_gestao_restaurantes

# Ver IP do container
docker inspect -f '{{.NetworkSettings.IPAddress}}' api_gestao_restaurantes
```

## 📈 Monitoramento

### Dashboard Docker

Se estiver usando Docker Desktop:

1. Abra a aplicação Docker Desktop
2. Vá em **Containers**
3. Encontre `api-gestao-gestaurantes`
4. Veja logs, recursos, etc.

### Verificar Uso de Recursos

```bash
docker stats

# Saída esperada:
# CONTAINER            CPU %      MEM USAGE / LIMIT      MEM %
# api_gestao_rest...   2.15%      650MiB / 4GiB          16.25%
# mysql_gestao_rest... 0.85%      320MiB / 4GiB          8%
```

## 📝 Exemplos de Configurações Alternativas

### Ambiente de Produção

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: mysql_prod
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASS}
      MYSQL_DATABASE: ${DB_NAME}
      MYSQL_USER: ${DB_USER}
      MYSQL_PASSWORD: ${DB_PASS}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - app-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping"]
      interval: 30s
      timeout: 10s
      retries: 5

  api:
    image: seu-registry/api-gestao:1.0.0 # Usar imagem pré-compilada
    container_name: api_prod
    depends_on:
      mysql:
        condition: service_healthy
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/${DB_NAME}?serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: ${DB_USER}
      SPRING_DATASOURCE_PASSWORD: ${DB_PASS}
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate # Nunca usar update em produção
    networks:
      - app-network
    restart: always

volumes:
  mysql_data:

networks:
  app-network:
```

## 🔄 Backup e Restore

### Backup do Banco de Dados

```bash
# Fazer backup
docker-compose exec mysql mysqldump -u app_user -p gestao_restaurantes > backup_$(date +%Y%m%d_%H%M%S).sql

# Sem interagir (senha em stdin)
echo 'app_password' | docker-compose exec -T mysql mysqldump -u app_user -p gestao_restaurantes > backup.sql
```

### Restore do Banco de Dados

```bash
# Restaurar backup
cat backup.sql | docker-compose exec -T mysql mysql -u app_user -p gestao_restaurantes
```

## 📚 Referências

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
- [MySQL Docker Hub](https://hub.docker.com/_/mysql)
- [OpenJDK Docker Hub](https://hub.docker.com/_/openjdk)

---

## ✅ Checklist de Execução

- [ ] Docker instalado e funcionando
- [ ] Docker Compose instalado
- [ ] Repositório clonado/acessado
- [ ] Executou `docker-compose up -d`
- [ ] Aguardou 2-3 minutos para compilação
- [ ] Verificou com `docker-compose ps`
- [ ] Acessou http://localhost:8080/swagger-ui.html
- [ ] Testou criar um usuário
- [ ] Testou listar usuários
- [ ] Testou login

---

**Fim da Documentação DeepWiki**
