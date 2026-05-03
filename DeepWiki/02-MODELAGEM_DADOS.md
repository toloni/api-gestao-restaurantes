# 02 - Modelagem das Entidades e Relacionamentos

## 📋 Visão Geral do Modelo de Dados

O modelo de dados da API Gestão de Restaurantes é relacional e centrado na entidade **Usuario**, que representa tanto clientes quanto donos de restaurantes no sistema.

## 📊 Diagrama Entidade-Relacionamento (ER)

```
┌─────────────────────────────────────────────────────────────┐
│                       USUARIO                                │
├─────────────────────────────────────────────────────────────┤
│ PK │ id                     │ BIGINT                         │
├────┼────────────────────────┼────────────────────────────────┤
│    │ nome                   │ VARCHAR(100)                   │
│    │ email                  │ VARCHAR(255) UNIQUE            │
│    │ login                  │ VARCHAR(50) UNIQUE             │
│    │ senha                  │ VARCHAR(255)                   │
│    │ tipo                   │ ENUM(CLIENTE|DONO_RESTAURANTE) │
│    │ endereco               │ VARCHAR(200)                   │
│    │ ultimaAlteracao        │ DATE                           │
└─────────────────────────────────────────────────────────────┘
```

## 🔍 Descrição Detalhada das Entidades

### Entidade: Usuario

A entidade `Usuario` representa um usuário do sistema que pode ser:
- **CLIENTE**: Pessoa que usa a plataforma para fazer pedidos
- **DONO_RESTAURANTE**: Pessoa que gerencia um restaurante

#### Mapeamento JPA

```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String login;

  @ToString.Exclude
  private String senha;

  @Enumerated(EnumType.STRING)
  private TipoUsuario tipo;

  private String endereco;
  private LocalDate ultimaAlteracao;
}
```

#### Atributos

| Campo | Tipo | Restrições | Descrição |
|-------|------|-----------|-----------|
| **id** | Long | PK, AUTO_INCREMENT | Identificador único do usuário |
| **nome** | String | NOT NULL, 2-100 caracteres | Nome completo do usuário |
| **email** | String | NOT NULL, UNIQUE | Email de contato (deve ser válido) |
| **login** | String | NOT NULL, UNIQUE, 3-50 caracteres | Login para autenticação |
| **senha** | String | NOT NULL, ≥6 caracteres | Senha (nunca retornada nas respostas) |
| **tipo** | TipoUsuario | NOT NULL, ENUM | CLIENTE ou DONO_RESTAURANTE |
| **endereco** | String | NOT NULL, 10-200 caracteres | Endereço completo |
| **ultimaAlteracao** | LocalDate | NULLABLE | Data da última alteração do perfil |

#### Restrições de Integridade

1. **Chave Primária**: `id` (auto-incremento)
2. **Chaves Únicas**: 
   - `email` (não pode haver dois usuários com mesmo email)
   - `login` (não pode haver dois usuários com mesmo login)
3. **Não Nulos**: 
   - nome, email, login, senha, tipo, endereco

### Enum: TipoUsuario

Representa os tipos de usuário permitidos no sistema.

```java
public enum TipoUsuario {
  CLIENTE,              // Usuário comum que faz pedidos
  DONO_RESTAURANTE     // Usuário que gerencia restaurante
}
```

#### Valores Permitidos

| Valor | Significado | Permissões Típicas |
|-------|----------|-------------------|
| **CLIENTE** | Cliente da plataforma | Fazer pedidos, avaliar |
| **DONO_RESTAURANTE** | Proprietário de restaurante | Gerenciar cardápio, visualizar pedidos |

## 📐 DTOs (Data Transfer Objects)

Os DTOs definem o contrato de comunicação entre cliente e servidor.

### UsuarioCreateDTO

Usado na **criação de um novo usuário** (POST).

```java
public record UsuarioCreateDTO(
  String nome,           // 2-100 caracteres
  String email,          // Formato email válido
  String login,          // 3-50 caracteres, padrão: ^[a-zA-Z0-9._-]+$
  String senha,          // Mínimo 6 caracteres
  String tipo,           // "CLIENTE" ou "DONO_RESTAURANTE"
  String endereco        // 10-200 caracteres
) { }
```

**Exemplo de Entrada:**
```json
{
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "login": "joao.silva",
  "senha": "senha123",
  "tipo": "CLIENTE",
  "endereco": "Rua Exemplo, 123 - Bairro, São Paulo - SP"
}
```

### UsuarioUpdateDTO

Usado na **atualização de dados do usuário** (PATCH).

```java
public record UsuarioUpdateDTO(
  String nome,           // 2-100 caracteres (opcional)
  String email,          // Formato email válido (opcional)
  String endereco        // 10-200 caracteres (opcional)
) { }
```

**Exemplo de Entrada:**
```json
{
  "nome": "João Silva Santos",
  "email": "joao.santos@example.com",
  "endereco": "Avenida Paulista, 1000 - São Paulo - SP"
}
```

### UsuarioResponseDTO

Retornado nas **respostas de consulta ou criação** (GET, POST).

```java
public record UsuarioResponseDTO(
  Long id,
  String nome,
  String email,
  String login,
  String tipo,
  String endereco,
  LocalDate ultimaAlteracao
) { }
```

**Exemplo de Saída:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "login": "joao.silva",
  "tipo": "CLIENTE",
  "endereco": "Rua Exemplo, 123 - Bairro, São Paulo - SP",
  "ultimaAlteracao": "2026-05-01"
}
```

⚠️ **Nota**: A senha **nunca** é retornada nas respostas por questões de segurança.

### LoginDTO

Usado na **autenticação do usuário** (POST /api/v1/login).

```java
public record LoginDTO(
  String login,          // 3-50 caracteres
  String senha           // Mínimo 6 caracteres
) { }
```

**Exemplo de Entrada:**
```json
{
  "login": "joao.silva",
  "senha": "senha123"
}
```

### AlterarSenhaDTO

Usado na **alteração de senha** (PATCH /api/v1/usuarios/{id}/alterar-senha).

```java
public record AlterarSenhaDTO(
  String senhaAtual,     // Senha atual (mínimo 6 caracteres)
  String novaSenha       // Nova senha (mínimo 6 caracteres)
) { }
```

**Exemplo de Entrada:**
```json
{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456"
}
```

## 🔄 Transformação de Dados

### Fluxo de Criação (POST)

```
UsuarioCreateDTO (entrada JSON)
         ↓ (Validação)
    UsuarioMapper.toEntity()
         ↓ (conversão)
    Usuario (entidade JPA)
         ↓ (Salvamento)
    Banco de Dados
         ↓ (Consulta)
    Usuario (com ID gerado)
         ↓ (UsuarioMapper.toResponseDTO())
UsuarioResponseDTO (resposta JSON)
```

### Fluxo de Atualização (PATCH)

```
UsuarioUpdateDTO (entrada JSON)
         ↓ (Validação)
UsuarioMapper.toEntityUpdate()
         ↓ (conversão)
    Usuario (entidade parcial)
         ↓ (Merge + Salvamento)
    Banco de Dados
         ↓ (sem retorno no PATCH)
    204 No Content
```

## 🔐 Validações

### Validações no UsuarioCreateDTO

| Campo | Validações | Exemplo |
|-------|-----------|---------|
| **nome** | NotBlank, Size(2-100) | "João Silva" |
| **email** | NotBlank, Email | "joao@example.com" |
| **login** | NotBlank, Size(3-50), Pattern | "joao_silva-123" |
| **senha** | NotBlank, Size(min=6) | "senha123" |
| **tipo** | NotBlank, Pattern | "CLIENTE" ou "DONO_RESTAURANTE" |
| **endereco** | NotBlank, Size(10-200) | "Rua X, 123 - Cidade - UF" |

### Validações Customizadas (Service Layer)

1. **Email Único**: Na criação e atualização
2. **Login Único**: Na criação e atualização
3. **Tipo Válido**: Deve ser CLIENTE ou DONO_RESTAURANTE
4. **Senha Atual Correta**: Na alteração de senha

## 📊 Relacionamentos

Atualmente, a modelagem contém **apenas uma entidade** (Usuario). Para contexto de um sistema de gestão de restaurantes, a arquitetura foi projetada para suportar futuras expansões:

### Possíveis Extensões Futuras

```
USUARIO
  ├─ (1:N) ─→ RESTAURANTE (se DONO_RESTAURANTE)
  │             ├─ (1:N) ─→ CARDAPIO
  │             │             └─ (1:N) ─→ ITEM_CARDAPIO
  │             └─ (1:N) ─→ PEDIDO
  │
  └─ (N:M) ─→ PEDIDO (se CLIENTE)
               └─ (1:N) ─→ ITEM_PEDIDO
```

Essa estrutura fut permitirá que a API evolua para gerenciar múltiplos restaurantes, cardápios e pedidos.

## 💾 Ciclo de Vida das Entidades

### Estados de Uma Entidade Usuario

```
Transiente (Novo objeto)
    ↓ (salvar())
Persistente (ID gerado)
    ↓ (alterarUsuario())
Persistente Modificado
    ↓ (flush/commit)
Persistente Sincronizado
    ↓ (excluir())
Removido
```

## 🗄️ Índices e Performance

Para otimizar consultas frequentes:

| Campo | Tipo | Razão |
|-------|------|-------|
| **id** | Primary Key | Identificação única rápida |
| **email** | Unique | Busca por email |
| **login** | Unique | Busca por login (autenticação) |

---

**Próximas seções**: [Endpoints](03-ENDPOINTS.md)
