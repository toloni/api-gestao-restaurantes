# DeepWiki - API Gestão de Restaurantes

## Documentação Técnica Completa

Bem-vindo ao DeepWiki da **API Gestão de Restaurantes**. Este diretório contém a documentação técnica completa do projeto, incluindo arquitetura, modelagem de dados, endpoints, integração com Swagger, coleção Postman e instruções de execução.

## 📚 Índice da Documentação

1. **[01-ARQUITETURA.md](01-ARQUITETURA.md)** - Descrição detalhada da arquitetura da aplicação
   - Stack tecnológico
   - Padrões de arquitetura
   - Fluxo de requisições
   - Componentes principais

2. **[02-MODELAGEM_DADOS.md](02-MODELAGEM_DADOS.md)** - Modelagem das entidades e relacionamentos
   - Diagrama ER (Entidade-Relacionamento)
   - Descrição das entidades
   - Atributos e restrições
   - Relacionamentos

3. **[03-ENDPOINTS.md](03-ENDPOINTS.md)** - Descrição dos endpoints disponíveis
   - Endpoint de Login
   - Endpoints de Usuários (CRUD)
   - Parâmetros, respostas e exemplos
   - Códigos de status HTTP

4. **[04-SWAGGER.md](04-SWAGGER.md)** - Documentação Swagger e OpenAPI
   - Configuração do Swagger
   - Visualização interativa
   - Definições de schemas
   - Exemplos de requisições e respostas

5. **[05-POSTMAN.md](05-POSTMAN.md)** - Coleção Postman e exemplos de uso
   - Como importar a coleção
   - Variáveis de ambiente
   - Exemplos de requisições
   - Scripts de testes

6. **[06-BANCO_DADOS.md](06-BANCO_DADOS.md)** - Estrutura do banco de dados
   - Tabelas e colunas
   - Chaves primárias e foreign keys
   - Índices
   - DDL (Data Definition Language)

7. **[07-DOCKER_COMPOSE.md](07-DOCKER_COMPOSE.md)** - Execução com Docker Compose
   - Variáveis de ambiente
   - Passo a passo para executar
   - Troubleshooting
   - Parada e limpeza dos containers

---

## 🚀 Início Rápido

Para começar a usar a API com Docker Compose:

```bash
# 1. Clonar ou acessar o repositório
cd api-gestao-gestaurantes

# 2. Executar com Docker Compose
docker-compose up -d

# 3. Acessar a API
# API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
# MySQL: localhost:3306

# 4. Parar os containers
docker-compose down
```

---

## 📋 Informações Gerais do Projeto

| Aspecto             | Detalhes                   |
| ------------------- | -------------------------- |
| **Nome do Projeto** | API Gestão de Restaurantes |
| **Versão**          | 1.0.0                      |
| **Java**            | 21                         |
| **Spring Boot**     | 4.0.5                      |
| **Banco de Dados**  | MySQL 8.0                  |
| **Build Tool**      | Maven 3.9.15               |
| **Containerização** | Docker & Docker Compose    |

---

## 📝 Resumo da Arquitetura

A API Gestão de Restaurantes utiliza uma **arquitetura em camadas (Layered Architecture)** com:

- **Camada de Apresentação**: Controllers REST com endpoints
- **Camada de Lógica de Negócio**: Services com validação e regras de negócio
- **Camada de Persistência**: Repository com Spring Data JPA
- **Camada de Dados**: MySQL database

---

## 🔐 Segurança

- Validação de entrada em todos os endpoints
- Exceções customizadas com tratamento global
- Senha armazenada com restrições
- Email e login únicos

---

## 📞 Contato e Suporte

Para dúvidas ou problemas, consulte a documentação específica em cada arquivo markdown deste diretório.

---

**Última atualização**: Maio de 2026
