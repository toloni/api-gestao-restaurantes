# 06 - Estrutura do Banco de Dados

## 📊 Visão Geral

O banco de dados da API Gestão de Restaurantes utiliza **MySQL 8.0** em produção. A estrutura é simples e escalável, centrada na tabela `usuario`.

## 🗄️ Configuração do Banco de Dados

### Banco de Dados Primário (MySQL)

```yaml
Ambiente: Produção / Docker Compose
SGBD: MySQL 8.0
Nome: gestao_restaurantes
Host: mysql (via Docker Compose)
Host: localhost:3306 (acesso local)
Usuário: app_user
Senha: app_password
```

### Banco de Dados Secundário (H2)

```yaml
Ambiente: Desenvolvimento / Testes
SGBD: H2 Database (em memória)
Nome: gestaorestaurantes
URL: jdbc:h2:mem:gestaorestaurantes
Usuário: sa
Senha: (vazia)
```

## 📋 Tabelas

### Tabela: usuario

Armazena dados de usuários do sistema.

#### DDL (Data Definition Language)

```sql
CREATE TABLE usuario (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  login VARCHAR(50) UNIQUE NOT NULL,
  senha VARCHAR(255) NOT NULL,
  tipo VARCHAR(50) NOT NULL ENUM('CLIENTE', 'DONO_RESTAURANTE'),
  endereco VARCHAR(200) NOT NULL,
  ultima_alteracao DATE,
  CONSTRAINT uk_email UNIQUE (email),
  CONSTRAINT uk_login UNIQUE (login),
  INDEX idx_email (email),
  INDEX idx_login (login),
  INDEX idx_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

#### Colunas

| Nome                 | Tipo    | Tamanho | Nulo | Padrão         | Descrição                               |
| -------------------- | ------- | ------- | ---- | -------------- | --------------------------------------- |
| **id**               | BIGINT  | 19      | NÃO  | AUTO_INCREMENT | Identificador único (Chave Primária)    |
| **nome**             | VARCHAR | 100     | NÃO  | -              | Nome completo do usuário                |
| **email**            | VARCHAR | 255     | NÃO  | -              | Email (Único)                           |
| **login**            | VARCHAR | 50      | NÃO  | -              | Login (Único)                           |
| **senha**            | VARCHAR | 255     | NÃO  | -              | Senha em texto (recomenda-se usar hash) |
| **tipo**             | ENUM    | -       | NÃO  | -              | CLIENTE ou DONO_RESTAURANTE             |
| **endereco**         | VARCHAR | 200     | NÃO  | -              | Endereço completo                       |
| **ultima_alteracao** | DATE    | -       | SIM  | NULL           | Data da última alteração                |

#### Restrições

1. **Chave Primária**

   ```sql
   PRIMARY KEY (id)
   ```

   - Identificador único
   - Auto-incremento

2. **Chaves Únicas**

   ```sql
   UNIQUE (email)
   UNIQUE (login)
   ```

   - Garante que não haja duplicatas
   - Permite buscas eficientes

3. **Não Nulo (NOT NULL)**
   - nome
   - email
   - login
   - senha
   - tipo
   - endereco

#### Índices

| Nome        | Tipo    | Colunas | Finalidade                           |
| ----------- | ------- | ------- | ------------------------------------ |
| `PRIMARY`   | PRIMARY | id      | Acesso rápido por ID                 |
| `uk_email`  | UNIQUE  | email   | Garantir unicidade; acesso por email |
| `uk_login`  | UNIQUE  | login   | Garantir unicidade; acesso por login |
| `idx_email` | INDEX   | email   | Otimizar buscas por email            |
| `idx_login` | INDEX   | login   | Otimizar buscas por login            |
| `idx_tipo`  | INDEX   | tipo    | Otimizar filtros por tipo            |

## 📊 Modelo Entidade-Relacionamento

Atualmente, a base de dados possui apenas uma tabela. Para futuras expansões:

```
USUARIO (atual)
├─ id (PK)
├─ nome
├─ email (UNIQUE)
├─ login (UNIQUE)
├─ senha
├─ tipo (ENUM)
├─ endereco
└─ ultima_alteracao

FUTURO: RESTAURANTE
├─ id (PK)
├─ usuario_id (FK → USUARIO.id)
├─ nome
├─ cnpj
├─ telefone
└─ endereco

FUTURO: CARDAPIO
├─ id (PK)
├─ restaurante_id (FK → RESTAURANTE.id)
├─ nome
└─ descricao

FUTURO: ITEM_CARDAPIO
├─ id (PK)
├─ cardapio_id (FK → CARDAPIO.id)
├─ nome
├─ descricao
├─ preco
└─ disponivel

FUTURO: PEDIDO
├─ id (PK)
├─ usuario_id (FK → USUARIO.id)
├─ restaurante_id (FK → RESTAURANTE.id)
├─ data_pedido
├─ status
└─ total
```

## 🔄 Ciclo de Vida dos Dados

### Criação de Usuário

```sql
-- Spring Boot executa automaticamente via Hibernate
INSERT INTO usuario (nome, email, login, senha, tipo, endereco, ultima_alteracao)
VALUES ('João Silva', 'joao@example.com', 'joao.silva', 'hashed_password', 'CLIENTE',
        'Rua Exemplo, 123', NULL);

-- Retorna: id = 1 (AUTO_INCREMENT)
```

### Leitura de Usuário

```sql
-- Consultar por ID
SELECT * FROM usuario WHERE id = 1;

-- Consultar por email
SELECT * FROM usuario WHERE email = 'joao@example.com';

-- Consultar por login (autenticação)
SELECT * FROM usuario WHERE login = 'joao.silva';

-- Listar com paginação
SELECT * FROM usuario LIMIT 10 OFFSET 0;

-- Filtrar por tipo
SELECT * FROM usuario WHERE tipo = 'CLIENTE' LIMIT 10;
```

### Atualização de Usuário

```sql
-- Atualizar dados
UPDATE usuario
SET nome = 'João Silva Santos',
    endereco = 'Avenida Paulista, 1000',
    ultima_alteracao = CURDATE()
WHERE id = 1;
```

### Alteração de Senha

```sql
-- Atualizar apenas senha
UPDATE usuario
SET senha = 'new_hashed_password'
WHERE id = 1;
```

### Exclusão de Usuário

```sql
-- Deletar usuário
DELETE FROM usuario WHERE id = 1;
```

## 🔐 Segurança do Banco de Dados

### 1. Credenciais

**Desenvolvimento (Docker Compose)**:

- Usuário: `app_user`
- Senha: `app_password`
- ⚠️ Não usar em produção

**Produção** (recomendações):

- Usar senhas fortes (mínimo 32 caracteres)
- Armazenar em variáveis de ambiente
- Rotacionar regularmente
- Usar autenticação via IAM (ex: AWS RDS)

### 2. Acesso ao Banco de Dados

**Local** (durante desenvolvimento):

```bash
mysql -h localhost -u app_user -p gestao_restaurantes
```

**Via Docker Compose**:

```bash
docker-compose exec mysql mysql -u app_user -p gestao_restaurantes
```

### 3. Backup e Restore

**Backup**:

```bash
# Via Docker
docker-compose exec mysql mysqldump -u app_user -p gestao_restaurantes > backup.sql

# Via MySQL client
mysqldump -h localhost -u app_user -p gestao_restaurantes > backup.sql
```

**Restore**:

```bash
# Via Docker
docker-compose exec -T mysql mysql -u app_user -p gestao_restaurantes < backup.sql

# Via MySQL client
mysql -h localhost -u app_user -p gestao_restaurantes < backup.sql
```

## 📈 Queries Úteis

### Verificar Estrutura

```sql
-- Descrever tabela
DESCRIBE usuario;

-- Ver índices
SHOW INDEXES FROM usuario;

-- Ver constraints
SELECT CONSTRAINT_NAME, COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'usuario';
```

### Estatísticas

```sql
-- Contar usuários
SELECT COUNT(*) as total FROM usuario;

-- Contar por tipo
SELECT tipo, COUNT(*) as total FROM usuario GROUP BY tipo;

-- Usuários mais antigos
SELECT * FROM usuario ORDER BY ultima_alteracao ASC LIMIT 10;

-- Usuários recentemente alterados
SELECT * FROM usuario ORDER BY ultima_alteracao DESC LIMIT 10;
```

### Busca e Filtro

```sql
-- Buscar por nome (parcial)
SELECT * FROM usuario WHERE nome LIKE '%João%';

-- Buscar por domínio de email
SELECT * FROM usuario WHERE email LIKE '%@example.com';

-- Filtrar por tipo
SELECT * FROM usuario WHERE tipo = 'DONO_RESTAURANTE';

-- Usuários sem alteração registrada
SELECT * FROM usuario WHERE ultima_alteracao IS NULL;

-- Usuários com alteração em data específica
SELECT * FROM usuario WHERE DATE(ultima_alteracao) = '2026-05-01';
```

### Validação de Integridade

```sql
-- Verificar emails duplicados
SELECT email, COUNT(*) as cnt FROM usuario GROUP BY email HAVING COUNT(*) > 1;

-- Verificar logins duplicados
SELECT login, COUNT(*) as cnt FROM usuario GROUP BY login HAVING COUNT(*) > 1;

-- Verificar registros com campos nulos obrigatórios
SELECT * FROM usuario WHERE nome IS NULL
   OR email IS NULL
   OR login IS NULL
   OR senha IS NULL
   OR tipo IS NULL
   OR endereco IS NULL;
```

## 🔄 Migrations e Versionamento

O projeto utiliza **Hibernate auto DDL** (`spring.jpa.hibernate.ddl-auto=update`).

```yaml
# application.properties
spring.jpa.hibernate.ddl-auto=update
```

Opções disponíveis:

| Valor           | Comportamento                                                     |
| --------------- | ----------------------------------------------------------------- |
| **validate**    | Apenas valida schema sem modificar                                |
| **update**      | Cria tabelas se não existirem; altera se houver mudanças (PADRÃO) |
| **create**      | Cria tabelas a cada inicialização (apaga dados)                   |
| **create-drop** | Cria ao iniciar; deleta ao finalizar                              |
| **none**        | Não faz nada                                                      |

⚠️ **Recomendação**: Para produção, usar `validate` e aplicar migrations manual com Flyway ou Liquibase.

## 📝 Exemplo de Dados

### Dados de Exemplo (Seed)

```sql
INSERT INTO usuario (nome, email, login, senha, tipo, endereco, ultima_alteracao)
VALUES
  ('João Silva', 'joao.silva@example.com', 'joao.silva', '$2a$10$...', 'CLIENTE',
   'Rua Exemplo, 123 - São Paulo - SP', '2026-05-01'),
  ('Maria Santos', 'maria.santos@example.com', 'maria.santos', '$2a$10$...', 'DONO_RESTAURANTE',
   'Avenida Paulista, 1000 - São Paulo - SP', '2026-04-28'),
  ('Pedro Oliveira', 'pedro.oliveira@example.com', 'pedro.oliveira', '$2a$10$...', 'CLIENTE',
   'Rua B, 456 - Rio de Janeiro - RJ', '2026-04-25');
```

## 🛠️ Ferramentas Úteis

### MySQL Client

```bash
# Conectar
mysql -h localhost -u app_user -p gestao_restaurantes

# Executar arquivo SQL
mysql -h localhost -u app_user -p gestao_restaurantes < script.sql
```

### DBeaver

- IDE gráfica para gerenciar banco de dados
- Suporta MySQL, PostgreSQL, Oracle, etc.
- Download: https://dbeaver.io/

### HeidiSQL

- Cliente leve e fácil para MySQL
- Download: https://www.heidisql.com/

### Adminer

- Web UI para gerenciar banco de dados
- Pode ser deployado via Docker

## 📚 Referências

- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Hibernate Documentation](https://hibernate.org/orm/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

---

**Próximas seções**: [Docker Compose](07-DOCKER_COMPOSE.md)
