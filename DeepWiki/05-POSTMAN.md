# 05 - Coleção Postman e Exemplos de Uso

## 📦 Visão Geral da Coleção Postman

A Coleção Postman fornece uma forma padronizada e reutilizável de testar todos os endpoints da API. A coleção está localizada em:

```
api-gestao-gestaurantes/Usuários.postman_collection.json
```

## 🔧 Como Importar a Coleção

### 1. Utilizando o Postman GUI

1. Abra o Postman
2. Clique em **File** → **Import** (ou use `Ctrl+O`)
3. Selecione **Upload Files**
4. Procure por `Usuários.postman_collection.json`
5. Clique em **Import**

### 2. Utilizando o Postman CLI

```bash
# Instalar Postman CLI (se necessário)
npm install -g @postmanlabs/postman

# Importar coleção
postman collection import "Usuários.postman_collection.json"
```

## 🌐 Variáveis de Ambiente

### Configuração de Variáveis

Para facilitar os testes, crie uma variável de ambiente:

**Nome**: `base_url`
**Valor**: `http://localhost:8080`

**Ou para produção**:
**Valor**: `https://seu-dominio.com`

### Como Criar Variáveis no Postman

1. Clique em **Environments** (lado esquerdo)
2. Clique em **Create New**
3. Nome: `Local Development`
4. Adicione a variável:
   - **VARIABLE**: `base_url`
   - **INITIAL VALUE**: `http://localhost:8080`
   - **CURRENT VALUE**: `http://localhost:8080`
5. Salve
6. Selecione o ambiente na dropdown (canto superior direito)

### Utilizando Variáveis em Requisições

Em vez de:

```
http://localhost:8080/api/v1/usuarios
```

Use:

```
{{base_url}}/api/v1/usuarios
```

## 📋 Estrutura da Coleção

A coleção está organizada em pastas:

```
Usuários.postman_collection.json
├── Login
│   └── POST Login
├── Usuários
│   ├── GET Listar Usuários
│   ├── GET Consultar por ID
│   ├── POST Criar Usuário
│   ├── PATCH Alterar Usuário
│   ├── DELETE Excluir Usuário
│   └── PATCH Alterar Senha
└── Scripts (Pre-request scripts e Tests)
```

## 🔐 Requisições da Coleção

### 1. Login

**Pasta**: Login
**Método**: POST
**URL**: `{{base_url}}/api/v1/login`

#### Body (JSON)

```json
{
  "login": "joao.silva",
  "senha": "senha123"
}
```

#### Resposta Esperada

```json
"Login realizado com sucesso"
```

---

### 2. Listar Usuários

**Pasta**: Usuários
**Método**: GET
**URL**: `{{base_url}}/api/v1/usuarios`

#### Query Parameters

```
nome: (opcional)
page: 1
size: 10
```

#### URL Completa

```
{{base_url}}/api/v1/usuarios?nome=João&page=1&size=10
```

#### Resposta Esperada

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
  }
]
```

---

### 3. Consultar Usuário por ID

**Pasta**: Usuários
**Método**: GET
**URL**: `{{base_url}}/api/v1/usuarios/1`

#### Path Variables

```
id: 1
```

#### Resposta Esperada

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

---

### 4. Criar Usuário

**Pasta**: Usuários
**Método**: POST
**URL**: `{{base_url}}/api/v1/usuarios`

#### Headers

```
Content-Type: application/json
```

#### Body (JSON)

```json
{
  "nome": "Maria Santos",
  "email": "maria.santos@example.com",
  "login": "maria.santos",
  "senha": "senha456",
  "tipo": "DONO_RESTAURANTE",
  "endereco": "Avenida Paulista, 1000 - São Paulo - SP"
}
```

#### Resposta Esperada (201 Created)

```json
{
  "id": 2,
  "nome": "Maria Santos",
  "email": "maria.santos@example.com",
  "login": "maria.santos",
  "tipo": "DONO_RESTAURANTE",
  "endereco": "Avenida Paulista, 1000 - São Paulo - SP",
  "ultimaAlteracao": null
}
```

---

### 5. Alterar Usuário

**Pasta**: Usuários
**Método**: PATCH
**URL**: `{{base_url}}/api/v1/usuarios/1`

#### Path Variables

```
id: 1
```

#### Headers

```
Content-Type: application/json
```

#### Body (JSON)

```json
{
  "nome": "João Silva Santos",
  "endereco": "Avenida Paulista, 500 - São Paulo - SP"
}
```

#### Resposta Esperada (204 No Content)

```
(sem body)
```

---

### 6. Excluir Usuário

**Pasta**: Usuários
**Método**: DELETE
**URL**: `{{base_url}}/api/v1/usuarios/2`

#### Path Variables

```
id: 2
```

#### Resposta Esperada (204 No Content)

```
(sem body)
```

---

### 7. Alterar Senha

**Pasta**: Usuários
**Método**: PATCH
**URL**: `{{base_url}}/api/v1/usuarios/1/alterar-senha`

#### Path Variables

```
id: 1
```

#### Headers

```
Content-Type: application/json
```

#### Body (JSON)

```json
{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha789"
}
```

#### Resposta Esperada (204 No Content)

```
(sem body)
```

## 🧪 Scripts de Teste (Tests)

Os testes automatizados no Postman validam as respostas:

### Exemplo de Test Script

```javascript
// Login - Validar resposta bem-sucedida
pm.test("Status code is 200", function () {
  pm.response.to.have.status(200);
});

pm.test("Response body contains success message", function () {
  pm.expect(pm.response.text()).to.include("sucesso");
});

// Criar Usuário - Validar estrutura de resposta
pm.test("Response has required fields", function () {
  var jsonData = pm.response.json();
  pm.expect(jsonData).to.have.property("id");
  pm.expect(jsonData).to.have.property("nome");
  pm.expect(jsonData).to.have.property("email");
  pm.expect(jsonData).to.have.property("login");
  pm.expect(jsonData).to.have.property("tipo");
  pm.expect(jsonData).to.have.property("endereco");
});

// Listar Usuários - Validar tipo de resposta
pm.test("Response is an array", function () {
  pm.expect(pm.response.json()).to.be.an("array");
});
```

## 📊 Fluxo de Teste Recomendado

### Cenário 1: Criar e Consultar Usuário

1. **POST /usuarios** - Criar novo usuário
   - Salve o ID retornado
2. **GET /usuarios/{id}** - Consultar o usuário criado
   - Valide que os dados foram salvos corretamente
3. **GET /usuarios** - Listar todos
   - Verifique que o novo usuário aparece na lista

### Cenário 2: Autenticar e Alterar Senha

1. **POST /login** - Login com credenciais atuais
   - Valide login bem-sucedido
2. **PATCH /usuarios/{id}/alterar-senha** - Alterar senha
   - Use a senha atual e defina nova senha
3. **POST /login** - Login com nova senha
   - Valide que a nova senha funciona
4. **POST /login** - Tentar login com senha antiga
   - Valide que a senha antiga não funciona mais

### Cenário 3: Alterar e Deletar Usuário

1. **PATCH /usuarios/{id}** - Alterar dados do usuário
   - Modifique nome, email, endereço
2. **GET /usuarios/{id}** - Consultar e validar alterações
   - Verifique que os dados foram atualizados
3. **DELETE /usuarios/{id}** - Deletar usuário
   - Status 204 esperado
4. **GET /usuarios/{id}** - Tentar consultar usuário deletado
   - Status 404 esperado (recurso não encontrado)

## 💾 Salvando Respostas

Para salvar a resposta de uma requisição:

1. Execute a requisição
2. Na seção **Response**, clique em **Save Response** (ícone de disquete)
3. Nomeie e salve

## 🔄 Ambiente de Pré-Requisição

Scripts de pré-requisição podem ser usados para preparar dados:

```javascript
// Exemplo: Gerar timestamp
var timestamp = new Date().getTime();
pm.environment.set("timestamp", timestamp);

// Exemplo: Gerar email aleatório
var email = "user" + timestamp + "@example.com";
pm.environment.set("random_email", email);
```

## 📈 Rodando Testes em Lote

### Via Postman CLI

```bash
# Rodar toda a coleção
postman collection run "Usuários.postman_collection.json" \
  -e ambiente.json \
  --reporters cli,json

# Com relatório HTML
postman collection run "Usuários.postman_collection.json" \
  --reporters htmlextra \
  --reporter-htmlextra-export results/report.html
```

### Via Newman (CLI do Postman)

```bash
# Instalar Newman
npm install -g newman
npm install -g newman-reporter-htmlextra

# Rodar coleção
newman run "Usuários.postman_collection.json" \
  -e ambiente.json \
  -r cli,htmlextra \
  --reporter-htmlextra-export ./results/report.html
```

## 🎯 Boas Práticas

1. **Use Variáveis**: Substitua valores hardcoded por variáveis
2. **Organize em Pastas**: Agrupe requisições relacionadas
3. **Adicione Testes**: Valide respostas com scripts
4. **Documente**: Adicione descrições nas requisições
5. **Reutilize**: Crie scripts pré-requisição para dados comuns

## 📚 Exemplo Completo de Fluxo

### Preparação

```javascript
// Pre-request Script
var timestamp = Date.now();
pm.environment.set("test_user_id", 1);
pm.environment.set("test_email", "test_" + timestamp + "@example.com");
```

### Request

```
POST {{base_url}}/api/v1/usuarios

{
  "nome": "Test User {{timestamp}}",
  "email": "{{test_email}}",
  "login": "testuser",
  "senha": "testpass123",
  "tipo": "CLIENTE",
  "endereco": "Rua Teste, 123 - São Paulo - SP"
}
```

### Test

```javascript
pm.test("Status is 201", function () {
  pm.response.to.have.status(201);
});

var jsonData = pm.response.json();
pm.environment.set("created_user_id", jsonData.id);
```

---

**Próximas seções**: [Banco de Dados](06-BANCO_DADOS.md)
