# 03 - Endpoints Disponíveis

## 🌐 Visão Geral dos Endpoints

A API possui dois grupos principais de endpoints:

1. **Login** - Autenticação de usuários
2. **Usuários** - Gerenciamento completo de usuários (CRUD)

**URL Base**: `http://localhost:8080/api/v1`

---

## 🔐 Endpoints de Login

### POST `/api/v1/login` - Realizar Login

Autentica um usuário verificando login e senha.

#### Especificação

| Aspecto | Valor |
|---------|-------|
| **Método HTTP** | POST |
| **Path** | `/api/v1/login` |
| **Descrição** | Autentica um usuário com base no login e senha fornecidos |
| **Autenticação** | Nenhuma (endpoint público) |
| **Responsável** | LoginController |

#### Requisição

**Headers**:
```
Content-Type: application/json
```

**Body** (JSON):
```json
{
  "login": "joao.silva",
  "senha": "senha123"
}
```

**Validações**:
- `login`: NotBlank, Size(3-50), Pattern([a-zA-Z0-9._-]+)
- `senha`: NotBlank, Size(min=6)

#### Resposta

**Sucesso (200 OK)**:
```
Status: 200 OK
Content-Type: application/json

"Login realizado com sucesso"
```

**Erro - Login/Senha Incorretos (401 Unauthorized)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Login ou senha incorretos",
  "path": "/api/v1/login"
}
```

**Erro - Usuário Não Encontrado (404 Not Found)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Usuário não encontrado",
  "path": "/api/v1/login"
}
```

**Erro - Validação Falhou (400 Bad Request)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erros de validação",
  "details": {
    "login": "Login é obrigatório",
    "senha": "Senha deve ter pelo menos 6 caracteres"
  }
}
```

#### Exemplos de Uso

**cURL**:
```bash
curl -X POST http://localhost:8080/api/v1/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "senha": "senha123"
  }'
```

**Python (requests)**:
```python
import requests

response = requests.post(
    'http://localhost:8080/api/v1/login',
    json={
        'login': 'joao.silva',
        'senha': 'senha123'
    }
)
print(response.status_code)
print(response.text)
```

**JavaScript (fetch)**:
```javascript
fetch('http://localhost:8080/api/v1/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    login: 'joao.silva',
    senha: 'senha123'
  })
})
.then(res => res.json())
.then(data => console.log(data));
```

---

## 👥 Endpoints de Usuários

### GET `/api/v1/usuarios` - Listar Usuários

Retorna uma lista paginada de usuários com filtro opcional por nome.

#### Especificação

| Aspecto | Valor |
|---------|-------|
| **Método HTTP** | GET |
| **Path** | `/api/v1/usuarios` |
| **Descrição** | Retorna uma lista paginada de usuários |
| **Autenticação** | Nenhuma (endpoint público) |

#### Query Parameters

| Parâmetro | Tipo | Padrão | Descrição |
|-----------|------|--------|-----------|
| `nome` | String | - | Filtro por nome (opcional) |
| `page` | Integer | 1 | Número da página (começa em 1) |
| `size` | Integer | 10 | Quantidade de resultados por página |

#### Requisição

**URL com filtros**:
```
GET http://localhost:8080/api/v1/usuarios?nome=João&page=1&size=10
```

#### Resposta

**Sucesso (200 OK)**:
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao.silva@example.com",
    "login": "joao.silva",
    "tipo": "CLIENTE",
    "endereco": "Rua Exemplo, 123 - São Paulo - SP",
    "ultimaAlteracao": "2026-05-01"
  },
  {
    "id": 2,
    "nome": "Maria Santos",
    "email": "maria.santos@example.com",
    "login": "maria.santos",
    "tipo": "DONO_RESTAURANTE",
    "endereco": "Avenida Paulista, 1000 - São Paulo - SP",
    "ultimaAlteracao": "2026-04-28"
  }
]
```

#### Exemplos de Uso

**cURL - Sem Filtros**:
```bash
curl -X GET http://localhost:8080/api/v1/usuarios
```

**cURL - Com Filtros**:
```bash
curl -X GET "http://localhost:8080/api/v1/usuarios?nome=João&page=1&size=5"
```

**JavaScript**:
```javascript
// Listar todos
fetch('http://localhost:8080/api/v1/usuarios')
  .then(res => res.json())
  .then(users => console.log(users));

// Com filtro
fetch('http://localhost:8080/api/v1/usuarios?nome=João&page=1&size=5')
  .then(res => res.json())
  .then(users => console.log(users));
```

---

### GET `/api/v1/usuarios/{id}` - Consultar Usuário por ID

Retorna os detalhes de um usuário específico.

#### Especificação

| Aspecto | Valor |
|---------|-------|
| **Método HTTP** | GET |
| **Path** | `/api/v1/usuarios/{id}` |
| **Descrição** | Retorna os detalhes de um usuário pelo ID |

#### Path Parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `id` | Long | ID do usuário a consultar |

#### Requisição

```
GET http://localhost:8080/api/v1/usuarios/1
```

#### Resposta

**Sucesso (200 OK)**:
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "login": "joao.silva",
  "tipo": "CLIENTE",
  "endereco": "Rua Exemplo, 123 - São Paulo - SP",
  "ultimaAlteracao": "2026-05-01"
}
```

**Erro - Usuário Não Encontrado (404 Not Found)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso não encontrado",
  "path": "/api/v1/usuarios/999"
}
```

#### Exemplos de Uso

**cURL**:
```bash
curl -X GET http://localhost:8080/api/v1/usuarios/1
```

**JavaScript**:
```javascript
fetch('http://localhost:8080/api/v1/usuarios/1')
  .then(res => res.json())
  .then(user => console.log(user));
```

---

### POST `/api/v1/usuarios` - Criar Usuário

Cria um novo usuário no sistema.

#### Especificação

| Aspecto | Valor |
|---------|-------|
| **Método HTTP** | POST |
| **Path** | `/api/v1/usuarios` |
| **Status Sucesso** | 201 Created |

#### Requisição

**Headers**:
```
Content-Type: application/json
```

**Body** (JSON):
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

#### Validações

| Campo | Restrições |
|-------|-----------|
| **nome** | NotBlank, Size(2-100) |
| **email** | NotBlank, Email válido, Único |
| **login** | NotBlank, Size(3-50), Pattern([a-zA-Z0-9._-]+), Único |
| **senha** | NotBlank, Size(min=6) |
| **tipo** | NotBlank, CLIENTE ou DONO_RESTAURANTE |
| **endereco** | NotBlank, Size(10-200) |

#### Resposta

**Sucesso (201 Created)**:
```
Status: 201 Created
Location: http://localhost:8080/api/v1/usuarios/1
Content-Type: application/json

{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "login": "joao.silva",
  "tipo": "CLIENTE",
  "endereco": "Rua Exemplo, 123 - Bairro, São Paulo - SP",
  "ultimaAlteracao": null
}
```

**Erro - Email Duplicado (409 Conflict)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Email já cadastrado no sistema",
  "path": "/api/v1/usuarios"
}
```

**Erro - Login Duplicado (409 Conflict)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Login já cadastrado no sistema",
  "path": "/api/v1/usuarios"
}
```

**Erro - Validação Falhou (400 Bad Request)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erros de validação",
  "details": {
    "nome": "Nome deve ter entre 2 e 100 caracteres",
    "email": "Email deve ter formato válido",
    "tipo": "Tipo deve ser CLIENTE ou DONO_RESTAURANTE"
  }
}
```

#### Exemplos de Uso

**cURL**:
```bash
curl -X POST http://localhost:8080/api/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao.silva@example.com",
    "login": "joao.silva",
    "senha": "senha123",
    "tipo": "CLIENTE",
    "endereco": "Rua Exemplo, 123 - Bairro, São Paulo - SP"
  }'
```

---

### PATCH `/api/v1/usuarios/{id}` - Alterar Usuário

Altera dados parciais de um usuário existente.

#### Especificação

| Aspecto | Valor |
|---------|-------|
| **Método HTTP** | PATCH |
| **Path** | `/api/v1/usuarios/{id}` |
| **Status Sucesso** | 204 No Content |

#### Path Parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `id` | Long | ID do usuário a alterar |

#### Requisição

**Body** (JSON - todos os campos são opcionais):
```json
{
  "nome": "João Silva Santos",
  "email": "joao.santos@example.com",
  "endereco": "Avenida Paulista, 1000 - São Paulo - SP"
}
```

#### Resposta

**Sucesso (204 No Content)**:
```
Status: 204 No Content
```

**Erro - Usuário Não Encontrado (404 Not Found)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso não encontrado",
  "path": "/api/v1/usuarios/999"
}
```

#### Exemplos de Uso

**cURL**:
```bash
curl -X PATCH http://localhost:8080/api/v1/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva Santos",
    "endereco": "Avenida Paulista, 1000 - São Paulo - SP"
  }'
```

---

### DELETE `/api/v1/usuarios/{id}` - Excluir Usuário

Exclui um usuário do sistema.

#### Especificação

| Aspecto | Valor |
|---------|-------|
| **Método HTTP** | DELETE |
| **Path** | `/api/v1/usuarios/{id}` |
| **Status Sucesso** | 204 No Content |

#### Path Parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `id` | Long | ID do usuário a excluir |

#### Requisição

```
DELETE http://localhost:8080/api/v1/usuarios/1
```

#### Resposta

**Sucesso (204 No Content)**:
```
Status: 204 No Content
```

**Erro - Usuário Não Encontrado (404 Not Found)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso não encontrado",
  "path": "/api/v1/usuarios/999"
}
```

#### Exemplos de Uso

**cURL**:
```bash
curl -X DELETE http://localhost:8080/api/v1/usuarios/1
```

---

### PATCH `/api/v1/usuarios/{id}/alterar-senha` - Alterar Senha

Permite que um usuário altere sua senha.

#### Especificação

| Aspecto | Valor |
|---------|-------|
| **Método HTTP** | PATCH |
| **Path** | `/api/v1/usuarios/{id}/alterar-senha` |
| **Status Sucesso** | 204 No Content |

#### Path Parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `id` | Long | ID do usuário cuja senha será alterada |

#### Requisição

**Headers**:
```
Content-Type: application/json
```

**Body** (JSON):
```json
{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456"
}
```

#### Validações

| Campo | Restrições |
|-------|-----------|
| **senhaAtual** | NotBlank, Size(min=6) |
| **novaSenha** | NotBlank, Size(min=6) |

#### Resposta

**Sucesso (204 No Content)**:
```
Status: 204 No Content
```

**Erro - Senha Atual Incorreta (401 Unauthorized)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Senha atual incorreta",
  "path": "/api/v1/usuarios/1/alterar-senha"
}
```

**Erro - Usuário Não Encontrado (404 Not Found)**:
```json
{
  "timestamp": "2026-05-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso não encontrado",
  "path": "/api/v1/usuarios/999/alterar-senha"
}
```

#### Exemplos de Uso

**cURL**:
```bash
curl -X PATCH http://localhost:8080/api/v1/usuarios/1/alterar-senha \
  -H "Content-Type: application/json" \
  -d '{
    "senhaAtual": "senha123",
    "novaSenha": "novaSenha456"
  }'
```

---

## 📊 Resumo dos Endpoints

| Método | Path | Descrição | Status Sucesso |
|--------|------|-----------|---|
| POST | `/api/v1/login` | Autenticar usuário | 200 OK |
| GET | `/api/v1/usuarios` | Listar usuários | 200 OK |
| GET | `/api/v1/usuarios/{id}` | Consultar por ID | 200 OK |
| POST | `/api/v1/usuarios` | Criar usuário | 201 Created |
| PATCH | `/api/v1/usuarios/{id}` | Alterar usuário | 204 No Content |
| DELETE | `/api/v1/usuarios/{id}` | Excluir usuário | 204 No Content |
| PATCH | `/api/v1/usuarios/{id}/alterar-senha` | Alterar senha | 204 No Content |

---

**Próximas seções**: [Swagger](04-SWAGGER.md)
