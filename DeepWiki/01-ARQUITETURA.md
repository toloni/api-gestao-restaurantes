# 01 - Arquitetura da Aplicação

## 📐 Visão Geral da Arquitetura

A **API Gestão de Restaurantes** segue o padrão de **Arquitetura em Camadas (Layered Architecture)**, que divide a aplicação em diferentes camadas, cada uma com responsabilidades específicas.

## 🏗️ Estrutura em Camadas

```
┌─────────────────────────────────────────────────────┐
│  Cliente (HTTP/REST)                                │
└───────────────────┬─────────────────────────────────┘
                    │ HTTP Requests/Responses
┌───────────────────▼─────────────────────────────────┐
│  CAMADA DE APRESENTAÇÃO (Presentation Layer)        │
│  • Controllers (UsuarioController, LoginController) │
│  • DTOs (Data Transfer Objects)                     │
│  • Validação de entrada                             │
└───────────────────┬─────────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────────┐
│  CAMADA DE APLICAÇÃO (Application Layer)            │
│  • Services (UsuarioService, LoginService)          │
│  • Mappers (UsuarioMapper)                          │
│  • Validadores customizados                         │
└───────────────────┬─────────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────────┐
│  CAMADA DE PERSISTÊNCIA (Persistence Layer)         │
│  • Repositories (UsuarioRepository)                 │
│  • Spring Data JPA                                  │
└───────────────────┬─────────────────────────────────┘
                    │ SQL Queries
┌───────────────────▼─────────────────────────────────┐
│  CAMADA DE DADOS (Data Layer)                       │
│  • MySQL Database (gestao_restaurantes)             │
└─────────────────────────────────────────────────────┘
```

## 🔧 Stack Tecnológico

### Backend

- **Java 21**: Linguagem de programação (LTS - Long Term Support)
- **Spring Boot 4.0.5**: Framework para construir aplicações Spring
- **Spring Data JPA**: Persistência de dados com abstração ORM
- **Spring Security**: Autenticação e autorização
- **Spring Validation**: Validação de dados de entrada

### Banco de Dados

- **MySQL 8.0**: SGBD relacional para produção
- **H2 Database**: SGBD em memória para desenvolvimento/testes

### Documentação e Desenvolvimento

- **SpringDoc OpenAPI 2.7.0**: Geração automática de Swagger/OpenAPI
- **Lombok**: Redução de boilerplate (getters, setters, construtores)

### Build e Containerização

- **Maven 3.9.15**: Gerenciador de dependências e build
- **Docker**: Containerização da aplicação
- **Docker Compose**: Orquestração de múltiplos containers

## 🌐 Fluxo de Requisições

### 1. Requisição HTTP entra no Spring Boot

```
Cliente → [GET/POST/PATCH/DELETE /api/v1/usuarios]
```

### 2. Passa pelo DispatcherServlet

O DispatcherServlet do Spring MVC intercepta e roteia a requisição

### 3. Chega ao Controller apropriado

- `LoginController` para `/api/v1/login`
- `UsuarioController` para `/api/v1/usuarios`

### 4. Validação de Entrada

- Anotações `@Valid` validam DTOs de entrada
- Validadores customizados executam regras específicas

### 5. Processamento no Service

- Lógica de negócio é executada
- Transformações de dados via Mappers

### 6. Acesso ao Banco de Dados

- Repository JPA executa operações de persistência
- Hibernate mapeia objetos Java para tabelas SQL

### 7. Resposta HTTP

- Service retorna dados formatados
- Controller constrói ResponseEntity com HTTP status apropriado

### 8. JSON é serializado

- Dados são convertidos para JSON
- Retorna ao cliente

## 📦 Componentes Principais

### Controllers

Responsáveis por receber requisições HTTP e orquestrar o processamento.

**UsuarioController** (`/api/v1/usuarios`)

- GET - Listar usuários (com paginação e filtro)
- GET /{id} - Consultar usuário por ID
- POST - Criar novo usuário
- PATCH /{id} - Alterar dados do usuário
- DELETE /{id} - Excluir usuário
- PATCH /{id}/alterar-senha - Alterar senha

**LoginController** (`/api/v1/login`)

- POST - Realizar login

### Services

Contêm a lógica de negócio da aplicação.

**UsuarioService**

- `listar(nome, page, size)` - Lista usuários com paginação e filtro
- `consultar(id)` - Consulta usuário por ID
- `salvar(usuario)` - Salva novo usuário
- `alterarUsuario(usuario)` - Altera dados do usuário
- `alterarSenha(id, senhaAtual, novaSenha)` - Altera senha
- `excluir(id)` - Exclui usuário

**LoginService**

- `login(login, senha)` - Autentica usuário

### Repositories

Abstraem a persistência de dados usando Spring Data JPA.

**UsuarioRepository**

- Herda de `JpaRepository<Usuario, Long>`
- Métodos customizados para consultas específicas

### DTOs (Data Transfer Objects)

Definem o contrato de comunicação com o cliente.

- **UsuarioCreateDTO** - Para criação de usuário
- **UsuarioUpdateDTO** - Para atualização de usuário
- **UsuarioResponseDTO** - Para resposta de usuário
- **LoginDTO** - Para autenticação
- **AlterarSenhaDTO** - Para alteração de senha

### Entidades (Domain)

Representam os objetos de negócio mapeados para o banco de dados.

**Usuario**

```
- id (Long): Identificador único (PK)
- nome (String): Nome completo
- email (String): Email (Único)
- login (String): Login (Único)
- senha (String): Senha
- tipo (TipoUsuario): CLIENTE ou DONO_RESTAURANTE
- endereco (String): Endereço
- ultimaAlteracao (LocalDate): Data última alteração
```

**TipoUsuario (Enum)**

- CLIENTE
- DONO_RESTAURANTE

### Mappers

Realizam conversão entre entidades e DTOs.

**UsuarioMapper**

- `toEntity(UsuarioCreateDTO)` → Converte DTO para entidade
- `toResponseDTO(Usuario)` → Converte entidade para DTO de resposta
- `toEntityUpdate(UsuarioUpdateDTO)` → Converte DTO para entidade de atualização

### Validadores

Implementam validações customizadas de negócio.

- **ValidadorEmailUnico** - Valida email único
- **ValidadorEmailUnicoParaAlteracao** - Valida email único em alteração
- **ValidadorLoginUnico** - Valida login único
- **ValidadorLoginUnicoParaAlteracao** - Valida login único em alteração
- **ValidadorTipoUsuario** - Valida tipos de usuário permitidos
- **ValidadorUsuario** - Validação genérica
- **ValidadorFactory** - Factory para criar validadores

### Exception Handler

Trata exceções e retorna respostas HTTP apropriadas.

**GlobalExceptionHandler**

- Captura exceções customizadas
- Retorna mensagens de erro formatadas com HTTP status apropriado

### Configurações

**OpenApiConfig**

- Configura documentação Swagger/OpenAPI
- Define título, descrição e versão da API

**SecurityConfig**

- Configura segurança da aplicação
- Desabilita autenticação automática do Spring Security

## 🔄 Padrões de Design Utilizados

### 1. **MVC (Model-View-Controller)**

- Model: Entidades (Usuario)
- View: DTOs (respostas JSON)
- Controller: UsuarioController, LoginController

### 2. **Service Layer Pattern**

- Abstrai lógica de negócio
- Reutilização de código
- Fácil testabilidade

### 3. **Repository Pattern**

- Abstrai persistência
- Facilita troca de banco de dados
- Testabilidade

### 4. **DTO Pattern**

- Desacopla estrutura interna da estrutura externa
- Segurança (não expõe todas as propriedades)
- Flexibilidade em alterações

### 5. **Factory Pattern**

- ValidadorFactory cria instâncias de validadores
- Centraliza criação de objetos

### 6. **Mapper Pattern**

- UsuarioMapper transforma entre objetos
- Responsabilidade única

## 🚀 Fluxo de Inicialização

```
1. Classe Application.main()
   └─ SpringApplication.run(Application.class, args)

2. Auto-configuration do Spring Boot
   └─ Carrega application.properties
   └─ Configura DataSource (MySQL)
   └─ Inicializa Spring Context

3. Beans são criados
   └─ UsuarioRepository
   └─ UsuarioService
   └─ UsuarioController
   └─ LoginService
   └─ LoginController

4. Tomcat é iniciado
   └─ Escuta na porta 8080

5. Aplicação está pronta
   └─ Aceita requisições HTTP
   └─ Swagger disponível em /swagger-ui.html
```

## 📊 Dependências entre Camadas

```
Controllers
    ↓ (injeção de dependência)
Services
    ↓ (injeção de dependência)
Repositories
    ↓ (queries SQL)
Database
```

As dependências só fluem para baixo, garantindo desacoplamento.

## 🔐 Segurança na Arquitetura

1. **Validação em Camadas**
   - Validação no Controller (anotações)
   - Validação no Service (lógica)
   - Validação no Repository (constraints BD)

2. **Exceções Customizadas**
   - Mensagens de erro controladas
   - Não expõe stack trace ao cliente

3. **Banco de Dados**
   - Constraints únicos no BD
   - Senha não retornada em respostas

4. **Autenticação**
   - Validação de login/senha no LoginService
   - Exceções específicas para falhas

## 📈 Escalabilidade

A arquitetura atual suporta:

- **Múltiplas instâncias** via containers Docker
- **Balanceamento de carga** (podem ser adicionados com nginx/haproxy)
- **Cache** (pode ser adicionado com Redis)
- **Fila de mensagens** (pode ser adicionado com RabbitMQ/Kafka)
- **Microserviços** (pode ser refatorado mantendo a mesma estrutura)

---

**Próximas seções**: [Modelagem de Dados](02-MODELAGEM_DADOS.md)
