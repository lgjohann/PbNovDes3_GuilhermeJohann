# PbNovDes3_GuilhermeJohann

# Microsserviço de Gerenciamento de Eventos


Microsserviço RESTful para gestão de eventos com integração de dados de localização via CEP.

---

## 📚 Documentação dos Endpoints

### 🎯 **Eventos**

#### **Criar Evento**
```http
POST /api/v1/events/create-event
```
_Cria um novo evento com validação de CEP_

**Corpo da Requisição:**
```json
{
  "eventName": "Nome do Evento",
  "dateTime": "2024-12-31T23:59:00",
  "cep": "01311-200"
}
```

**Respostas:**
| Código | Descrição                         |
|--------|-----------------------------------|
| 201    | Evento criado com sucesso         |
| 404    | CEP não encontrado                |
| 422    | Dados inválidos ou formato errado |

---

#### **Listar Todos os Eventos**
```http
GET /api/v1/events/get-all-events
```
_Retorna todos os eventos cadastrados_

**Respostas:**
| Código | Descrição                   |
|--------|-----------------------------|
| 200    | Lista de eventos            |
| 404    | Nenhum evento encontrado    |

---

#### **Listar Eventos Ordenados**
```http
GET /api/v1/events/get-all-events/sorted
```
_Lista eventos em ordem alfabética_

**Respostas:**
| Código | Descrição                   |
|--------|-----------------------------|
| 200    | Lista ordenada de eventos   |
| 404    | Nenhum evento encontrado    |

---

#### **Buscar Evento por ID**
```http
GET /api/v1/events/get-event/{id}
```
_Recupera detalhes de um evento específico_

**Parâmetro:**
- `id` (UUID do evento)

**Respostas:**
| Código | Descrição               |
|--------|-------------------------|
| 200    | Detalhes do evento      |
| 404    | Evento não encontrado   |

---

#### **Atualizar Evento**
```http
PUT /api/v1/events/update-event/{id}
```
_Atualiza informações de um evento existente_

**Parâmetro:**
- `id` (UUID do evento)

**Corpo da Requisição:**
```json
{
  "eventName": "Novo Nome",
  "dateTime": "2025-01-01T00:00:00",
  "cep": "20021-240"
}
```

**Respostas:**
| Código | Descrição                         |
|--------|-----------------------------------|
| 200    | Evento atualizado com sucesso     |
| 404    | Evento/CEP não encontrado         |
| 422    | Dados inválidos ou formato errado |

---

#### **Excluir Evento**
```http
DELETE /api/v1/events/delete-event/{id}
```
_Remove permanentemente um evento_

**Parâmetro:**
- `id` (UUID do evento)

**Respostas:**
| Código | Descrição                          |
|--------|------------------------------------|
| 204    | Exclusão bem-sucedida              |
| 404    | Evento não encontrado              |
| 409    | Evento com ingressos vendidos      |

---

## 📦 Estruturas de Dados

### **Entrada (EventCreateDto)**
```json
{
  "eventName": "string (obrigatório)",
  "dateTime": "string (formato ISO 8601)",
  "cep": "string (formato #####-###)"
}
```

### **Resposta (EventResponseDto)**
```json
{
  "id": "string",
  "eventName": "string",
  "dateTime": "string",
  "cep": "string",
  "logradouro": "string",
  "bairro": "string",
  "cidade": "string",
  "uf": "string"
}
```

### **Erro (ErrorMessage)**
```json
{
  "path": "string",
  "method": "string",
  "status": "number",
  "statusText": "string",
  "message": "string",
  "errors": {
    "campo": "descrição do erro"
  }
}
```

---

## ⚠️ Requisitos e Formatação

1. **Formato de Data/Hora:**  
   `YYYY-MM-DDTHH:mm:ss` (Ex: `2024-12-31T23:59:59`)

2. **Formato de CEP:**  
   `#####-###` (Ex: `01311-200`)

3. **Campos Obrigatórios para Criação:**
    - `eventName`
    - `dateTime`
    - `cep`

---

## 🌐 Endereço Base
`http://3.145.162.214:8080`

> **Nota Técnica:** Para operações de atualização/exclusão, o sistema valida:
> - Existência do evento
> - Formatação correta do CEP
> - Restrições de exclusão para eventos com ingressos vendidos
