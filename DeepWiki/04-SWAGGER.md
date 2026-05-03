# 04 - Documentação Swagger e OpenAPI

## 📖 Visão Geral do Swagger

A API Gestão de Restaurantes utiliza **SpringDoc OpenAPI** para gerar automaticamente documentação interativa através do Swagger UI.

## 🔧 Configuração do Swagger

### Classe OpenApiConfig

```java
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API Gestão de Restaurantes")
            .description("API para gerenciamento de usuários e operações do sistema")
            .version("1.0.0"));
  }
}
```

### Dependência Maven

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

## 🌐 Acessando a Documentação Swagger

Com a aplicação em execução, a documentação Swagger pode ser acessada em:

```
http://localhost:8080/swagger-ui.html
```

### URLs da Documentação

| Recurso          | URL                                    |
| ---------------- | -------------------------------------- |
| **Swagger UI**   | http://localhost:8080/swagger-ui.html  |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs      |
| **OpenAPI YAML** | http://localhost:8080/v3/api-docs.yaml |

## 📋 Estrutura da Documentação OpenAPI

### Exemplo de Definição OpenAPI (JSON)

```json
{
  "openapi": "3.0.1",
  "info": {
    "title": "API Gestão de Restaurantes",
    "description": "API para gerenciamento de usuários e operações do sistema",
    "version": "1.0.0"
  },
  "servers": [
    {
      "url": "http://localhost:8080",
      "description": "Development Server"
    }
  ],
  "paths": {
    "/api/v1/login": {
      "post": {
        "tags": ["Login"],
        "summary": "Realizar login",
        "description": "Autentica um usuário com base no login e senha fornecidos",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/LoginDTO"
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "Login realizado com sucesso",
            "content": {
              "application/json": {
                "schema": {
                  "type": "string"
                }
              }
            }
          },
          "401": {
            "description": "Login ou senha incorretos"
          },
          "404": {
            "description": "Usuário não encontrado"
          }
        }
      }
    },
    "/api/v1/usuarios": {
      "get": {
        "tags": ["Usuários"],
        "summary": "Listar usuários",
        "description": "Retorna uma lista paginada de usuários, com opção de filtro por nome",
        "parameters": [
          {
            "name": "nome",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "page",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "default": 1
            }
          },
          {
            "name": "size",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "default": 10
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Lista de usuários",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/UsuarioResponseDTO"
                  }
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": ["Usuários"],
        "summary": "Criar usuário",
        "description": "Cria um novo usuário no sistema. O email deve ser único.",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/UsuarioCreateDTO"
              }
            }
          }
        },
        "responses": {
          "201": {
            "description": "Usuário criado com sucesso",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UsuarioResponseDTO"
                }
              }
            }
          },
          "400": {
            "description": "Erro de validação"
          },
          "409": {
            "description": "Email ou login já cadastrado"
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "LoginDTO": {
        "type": "object",
        "properties": {
          "login": {
            "type": "string",
            "description": "Login do usuário",
            "example": "joao.silva"
          },
          "senha": {
            "type": "string",
            "description": "Senha do usuário",
            "example": "senha123"
          }
        },
        "required": ["login", "senha"]
      },
      "UsuarioCreateDTO": {
        "type": "object",
        "properties": {
          "nome": {
            "type": "string",
            "description": "Nome completo do usuário",
            "example": "João Silva"
          },
          "email": {
            "type": "string",
            "description": "Email do usuário (único)",
            "example": "joao.silva@example.com"
          },
          "login": {
            "type": "string",
            "description": "Login do usuário (único)",
            "example": "joao.silva"
          },
          "senha": {
            "type": "string",
            "description": "Senha do usuário",
            "example": "senha123"
          },
          "tipo": {
            "type": "string",
            "description": "Tipo do usuário",
            "enum": ["CLIENTE", "DONO_RESTAURANTE"],
            "example": "CLIENTE"
          },
          "endereco": {
            "type": "string",
            "description": "Endereço completo",
            "example": "Rua Exemplo, 123 - São Paulo - SP"
          }
        },
        "required": ["nome", "email", "login", "senha", "tipo", "endereco"]
      },
      "UsuarioResponseDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64",
            "description": "ID do usuário"
          },
          "nome": {
            "type": "string",
            "description": "Nome do usuário"
          },
          "email": {
            "type": "string",
            "description": "Email do usuário"
          },
          "login": {
            "type": "string",
            "description": "Login do usuário"
          },
          "tipo": {
            "type": "string",
            "description": "Tipo do usuário",
            "enum": ["CLIENTE", "DONO_RESTAURANTE"]
          },
          "endereco": {
            "type": "string",
            "description": "Endereço do usuário"
          },
          "ultimaAlteracao": {
            "type": "string",
            "format": "date",
            "description": "Data da última alteração"
          }
        }
      }
    }
  }
}
```

## 🏷️ Tags no Swagger

Os endpoints estão organizados em duas categorias:

### Tag: Login

Agrupa endpoints de autenticação do sistema.

```java
@Tag(name = "Login", description = "Autenticação de usuários no sistema")
public class LoginController { }
```

**Endpoints**:

- POST `/api/v1/login` - Realizar login

### Tag: Usuários

Agrupa endpoints de gerenciamento de usuários.

```java
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController { }
```

**Endpoints**:

- GET `/api/v1/usuarios`
- GET `/api/v1/usuarios/{id}`
- POST `/api/v1/usuarios`
- PATCH `/api/v1/usuarios/{id}`
- DELETE `/api/v1/usuarios/{id}`
- PATCH `/api/v1/usuarios/{id}/alterar-senha`

## 📝 Anotações Swagger Utilizadas

### @Operation

Define metadados de um endpoint específico.

```java
@Operation(
  summary = "Realizar login",
  description = "Autentica um usuário com base no login e senha fornecidos"
)
@PostMapping
public ResponseEntity<String> postMethodName(@RequestBody LoginDTO loginDTO) {
  // ...
}
```

### @Schema

Define metadados de um DTO ou propriedade.

```java
@Schema(description = "Nome completo do usuário", example = "João Silva")
String nome
```

## 🔍 Explorando a Documentação Swagger

### Interface do Swagger UI

A interface do Swagger oferece:

1. **Listagem de Endpoints**: Todos os endpoints agrupados por tag
2. **Detalhes de Cada Endpoint**:
   - Descrição e resumo
   - Parâmetros (path, query, body)
   - Schema de requisição
   - Schema de resposta
   - Códigos de status HTTP

3. **"Try it Out"**: Teste interativo dos endpoints
   - Preenchimento de parâmetros
   - Envio de requisições
   - Visualização de respostas

### Testando um Endpoint no Swagger

1. Acesse `http://localhost:8080/swagger-ui.html`
2. Encontre o endpoint desejado (ex: POST /api/v1/usuarios)
3. Clique em "Try it out"
4. Preencha os parâmetros/body
5. Clique em "Execute"
6. Visualize a resposta na seção "Response"

## 📊 Schemas de Componentes

### LoginDTO Schema

```json
{
  "type": "object",
  "required": ["login", "senha"],
  "properties": {
    "login": {
      "type": "string",
      "description": "Login do usuário (único)",
      "example": "joao.silva",
      "minLength": 3,
      "maxLength": 50,
      "pattern": "^[a-zA-Z0-9._-]+$"
    },
    "senha": {
      "type": "string",
      "description": "Senha do usuário",
      "example": "senha123",
      "minLength": 6
    }
  }
}
```

### UsuarioCreateDTO Schema

```json
{
  "type": "object",
  "required": ["nome", "email", "login", "senha", "tipo", "endereco"],
  "properties": {
    "nome": {
      "type": "string",
      "description": "Nome completo do usuário",
      "example": "João Silva",
      "minLength": 2,
      "maxLength": 100
    },
    "email": {
      "type": "string",
      "format": "email",
      "description": "Email do usuário (único)",
      "example": "joao.silva@example.com"
    },
    "login": {
      "type": "string",
      "description": "Login do usuário (único)",
      "example": "joao.silva",
      "minLength": 3,
      "maxLength": 50,
      "pattern": "^[a-zA-Z0-9._-]+$"
    },
    "senha": {
      "type": "string",
      "description": "Senha do usuário",
      "example": "senha123",
      "minLength": 6
    },
    "tipo": {
      "type": "string",
      "description": "Tipo do usuário",
      "enum": ["CLIENTE", "DONO_RESTAURANTE"],
      "example": "CLIENTE"
    },
    "endereco": {
      "type": "string",
      "description": "Endereço completo",
      "example": "Rua Exemplo, 123 - São Paulo - SP",
      "minLength": 10,
      "maxLength": 200
    }
  }
}
```

### UsuarioResponseDTO Schema

```json
{
  "type": "object",
  "properties": {
    "id": {
      "type": "integer",
      "format": "int64",
      "description": "ID do usuário",
      "example": 1
    },
    "nome": {
      "type": "string",
      "description": "Nome do usuário",
      "example": "João Silva"
    },
    "email": {
      "type": "string",
      "description": "Email do usuário",
      "example": "joao.silva@example.com"
    },
    "login": {
      "type": "string",
      "description": "Login do usuário",
      "example": "joao.silva"
    },
    "tipo": {
      "type": "string",
      "description": "Tipo do usuário",
      "enum": ["CLIENTE", "DONO_RESTAURANTE"],
      "example": "CLIENTE"
    },
    "endereco": {
      "type": "string",
      "description": "Endereço do usuário",
      "example": "Rua Exemplo, 123 - São Paulo - SP"
    },
    "ultimaAlteracao": {
      "type": "string",
      "format": "date",
      "description": "Data da última alteração",
      "example": "2026-05-01"
    }
  }
}
```

## 🔗 Referência Cruzada

Todos os endpoints estão documentados nos seguintes locais:

- **Arquivo**: [03-ENDPOINTS.md](03-ENDPOINTS.md) - Documentação detalhada manual
- **Interface Web**: `http://localhost:8080/swagger-ui.html` - UI interativa
- **JSON**: `http://localhost:8080/v3/api-docs` - Especificação em JSON
- **YAML**: `http://localhost:8080/v3/api-docs.yaml` - Especificação em YAML

## 📱 Cliente Swagger

A UI do Swagger permite:

✅ Visualizar todos os endpoints
✅ Testar requisições diretamente
✅ Ver exemplos de requisição e resposta
✅ Entender schema de dados
✅ Verificar validações

## 📚 Recursos Adicionais

### Documentação Oficial

- [SpringDoc OpenAPI](https://springdoc.org/)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)

### Exemplo de Requisição via cURL

```bash
# Copiar requisição do Swagger
# O Swagger mostra o comando cURL equivalente
```

---

**Próximas seções**: [Postman](05-POSTMAN.md)
