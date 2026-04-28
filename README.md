# API Gestão de Restaurantes

## Descrição

A API Gestão de Restaurantes é uma aplicação RESTful desenvolvida em Spring Boot para gerenciar usuários em um sistema de gestão de restaurantes. Permite operações de CRUD (Create, Read, Update, Delete) para usuários, autenticação via login e alteração de senhas, com suporte a diferentes tipos de usuários (Cliente e Dono de Restaurante).

## Objetivo

O objetivo desta API é fornecer uma base sólida para o gerenciamento de usuários em um sistema de gestão de restaurantes, garantindo segurança, validação de dados e integração com banco de dados. Ela serve como backend para aplicações front-end ou outros serviços que necessitam de controle de acesso e gerenciamento de perfis de usuário.

## Tecnologias Utilizadas

- **Java 21**: Linguagem de programação principal.
- **Spring Boot 4.0.5**: Framework para desenvolvimento de aplicações Java.
- **Spring Data JPA**: Para persistência de dados e mapeamento objeto-relacional.
- **Spring Security**: Para autenticação e autorização.
- **Spring Validation**: Para validação de dados de entrada.
- **H2 Database**: Banco de dados em memória para desenvolvimento e testes (configurado por padrão).
- **MySQL**: Banco de dados relacional para produção (configurado via Docker Compose).
- **Lombok**: Para redução de código boilerplate.
- **SpringDoc OpenAPI**: Para geração automática de documentação Swagger.
- **Maven**: Gerenciador de dependências e build.
- **Docker**: Containerização da aplicação.
- **Docker Compose**: Orquestração de containers (API + MySQL).

## Estrutura do Projeto

```
api-gestao-gestaurantes/
├── docker-compose.yml          # Configuração Docker Compose
├── Dockerfile                  # Dockerfile para build da imagem
├── HELP.md                     # Documentação auxiliar do Spring Boot
├── mvnw / mvnw.cmd             # Scripts Maven wrapper
├── pom.xml                     # Arquivo de configuração Maven
├── requests-login.http         # Arquivos de exemplo de requisições HTTP
├── requests-usuarios.http
└── src/
    ├── main/
    │   ├── java/br/com/toloni/gestaorestaurantes/
    │   │   ├── Application.java                    # Classe principal
    │   │   ├── config/
    │   │   │   ├── OpenApiConfig.java              # Configuração Swagger
    │   │   │   └── SecurityConfig.java             # Configuração de segurança
    │   │   ├── controller/
    │   │   │   ├── LoginController.java            # Controller de login
    │   │   │   ├── UsuarioController.java          # Controller de usuários
    │   │   │   └── dto/                            # Data Transfer Objects
    │   │   │       ├── AlterarSenhaDTO.java
    │   │   │       ├── LoginDTO.java
    │   │   │       ├── UsuarioCreateDTO.java
    │   │   │       ├── UsuarioResponseDTO.java
    │   │   │       └── UsuarioUpdateDTO.java
    │   │   ├── domain/
    │   │   │   ├── TipoUsuario.java                # Enum de tipos de usuário
    │   │   │   └── Usuario.java                    # Entidade Usuario
    │   │   ├── exception/
    │   │   │   ├── custom/                         # Exceções customizadas
    │   │   │   └── handler/                        # Handler global de exceções
    │   │   ├── mapper/
    │   │   │   └── UsuarioMapper.java              # Mapper para DTOs
    │   │   ├── repository/
    │   │   │   └── UsuarioRepository.java          # Repositório JPA
    │   │   ├── service/
    │   │   │   ├── LoginService.java / LoginServiceImp.java
    │   │   │   ├── UsuarioService.java / UsuarioServiceImp.java
    │   │   │   └── validador/                      # Validadores customizados
    │   └── resources/
    │       └── application.properties              # Configurações da aplicação
    └── test/
        └── java/br/com/toloni/gestaorestaurantes/
            └── GestaorestaurantesApplicationTests.java  # Testes unitários
```

## Endpoints da API

### Usuários (`/api/v1/usuarios`)

- **GET /api/v1/usuarios**
  - Descrição: Lista usuários paginados, com filtro opcional por nome.
  - Parâmetros de query: `nome` (opcional), `page` (padrão: 1), `size` (padrão: 10).
  - Resposta: Lista de `UsuarioResponseDTO`.

- **GET /api/v1/usuarios/{id}**
  - Descrição: Consulta usuário por ID.
  - Parâmetros: `id` (path).
  - Resposta: `UsuarioResponseDTO`.

- **POST /api/v1/usuarios**
  - Descrição: Cria um novo usuário.
  - Corpo: `UsuarioCreateDTO`.
  - Resposta: `UsuarioResponseDTO` (201 Created).

- **PATCH /api/v1/usuarios/{id}**
  - Descrição: Altera dados de um usuário.
  - Parâmetros: `id` (path).
  - Corpo: `UsuarioUpdateDTO`.
  - Resposta: 204 No Content.

- **DELETE /api/v1/usuarios/{id}**
  - Descrição: Exclui um usuário.
  - Parâmetros: `id` (path).
  - Resposta: 204 No Content.

- **PATCH /api/v1/usuarios/{id}/alterar-senha**
  - Descrição: Altera a senha do usuário.
  - Parâmetros: `id` (path).
  - Corpo: `AlterarSenhaDTO`.
  - Resposta: 204 No Content.

### Login (`/api/v1/login`)

- **POST /api/v1/login**
  - Descrição: Realiza login do usuário.
  - Corpo: `LoginDTO`.
  - Resposta: String "Login realizado com sucesso" (200 OK).

## Campos da Entidade Usuário

A entidade `Usuario` possui os seguintes campos:

- `id` (Long): Identificador único, gerado automaticamente.
- `nome` (String): Nome completo do usuário.
- `email` (String): Endereço de e-mail, único no sistema.
- `login` (String): Nome de usuário para login, único no sistema.
- `senha` (String): Senha criptografada do usuário.
- `tipo` (TipoUsuario): Tipo de usuário (CLIENTE ou DONO_RESTAURANTE).
- `endereco` (String): Endereço do usuário.
- `ultimaAlteracao` (LocalDate): Data da última alteração do registro.

## Configuração do Banco de Dados

A aplicação suporta dois bancos de dados principais:

### H2 Database (Desenvolvimento/Local)

- **Uso**: Configurado por padrão para desenvolvimento e testes locais.
- **Tipo**: Banco em memória (dados não persistidos entre reinicializações).
- **Console**: Acessível em `http://localhost:8080/h2-console` com as credenciais:
  - JDBC URL: `jdbc:h2:mem:gestaorestaurantes`
  - Username: `sa`
  - Password: (vazio)
- **Configuração**: No `application.properties`, as linhas para H2 estão ativas.

### MySQL Database (Produção/Docker)

- **Uso**: Recomendado para produção e execução com Docker.
- **Configuração via Variáveis de Ambiente**:
  - `SPRING_DATASOURCE_URL`: URL de conexão JDBC (ex: `jdbc:mysql://mysql:3306/gestao_restaurantes?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false`)
  - `SPRING_DATASOURCE_USERNAME`: Usuário do banco (ex: `app_user`)
  - `SPRING_DATASOURCE_PASSWORD`: Senha do banco (ex: `app_password`)
  - `SPRING_JPA_HIBERNATE_DDL_AUTO`: Estratégia de criação de tabelas (ex: `update`)
- **Para execução local com MySQL**:
  1. Instale e configure um servidor MySQL local.
  2. No `application.properties`, comente as linhas do H2 e descomente as linhas do MySQL.
  3. Configure as variáveis de ambiente ou edite diretamente no arquivo.
- **Docker Compose**: Automaticamente configura o MySQL com as variáveis necessárias.

## Como Executar

### Pré-requisitos

- Java 21
- Maven 3.9+
- Docker e Docker Compose (para execução com MySQL)

### Execução Local (com H2)

1. Clone o repositório.
2. Navegue até a pasta `api-gestao-gestaurantes`.
3. Execute: `mvn spring-boot:run`
4. A aplicação estará disponível em `http://localhost:8080`
5. O banco H2 estará ativo em memória (dados resetados a cada reinicialização).

### Execução Local (com MySQL)

1. Instale e configure um servidor MySQL local.
2. No arquivo `src/main/resources/application.properties`, comente as linhas do H2 e descomente as linhas do MySQL.
3. Configure as variáveis de ambiente ou edite diretamente as propriedades do MySQL.
4. Execute: `mvn spring-boot:run`
5. A aplicação estará disponível em `http://localhost:8080`

### Execução com Docker

1. Clone o repositório.
2. Navegue até a pasta `api-gestao-gestaurantes`.
3. Execute: `docker-compose up --build`
4. A aplicação estará disponível em `http://localhost:8080`
5. O MySQL estará disponível em `localhost:3306` (dados persistidos via volume).

## Documentação da API (Swagger)

A documentação interativa da API está disponível via Swagger UI em:

- **URL**: `http://localhost:8080/swagger-ui/index.html`

A documentação OpenAPI pode ser acessada em:

- **URL**: `http://localhost:8080/v3/api-docs`

## Outras Informações

- **Segurança**: Implementada com Spring Security. Autenticação básica via endpoint de login.
- **Validação**: Usa Bean Validation para validar entradas. Exceções customizadas são tratadas globalmente.
- **Logs**: Usa SLF4J com Logback para logging estruturado.
- **Arquivos de Requisição**: `requests-login.http` e `requests-usuarios.http` contêm exemplos de requisições HTTP para teste.
- **Versionamento**: API versionada em `v1` (`/api/v1/`).
- **Paginação**: Endpoints de listagem suportam paginação e filtros.

Para mais detalhes, consulte a documentação do Spring Boot em `HELP.md`.</content>
<parameter name="filePath">/Users/tiagotoloni/Documents/fiap/techChallenge/api-gestao-gestaurantes/README.md
