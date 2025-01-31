# Desafio 03 - Guilherme Johann

## Sobre o projeto
O projeto consiste em um sistema com arquitetura de microsserviços para gerenciamento de Eventos e Ingressos. Foram utilizados no projeto
tecnologias como o RabbitMQ e OpenFeign para comunicação entre os serviços, e Docker para facilidade de build e escalabilidade do sistema.

O projeto encontra-se hospedado na AWS, conforme endereços de acesso que estão ao fim da seção de seu respectivo microsserviço aqui no README.

## 🛠 Tecnologias Utilizadas

<table>
  <tr>
    <td align="center">
      <a href="https://www.java.com" target="_blank">
        <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java">
      </a>
    </td>
    <td align="center">
      <a href="https://spring.io/projects/spring-boot" target="_blank">
        <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot">
      </a>
    </td>
    <td align="center">
      <a href="https://www.mongodb.com" target="_blank">
        <img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB">
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://www.rabbitmq.com" target="_blank">
        <img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white" alt="RabbitMQ">
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/OpenFeign/feign" target="_blank">
        <img src="https://img.shields.io/badge/OpenFeign-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="OpenFeign">
      </a>
    </td>
    <td align="center">
      <a href="https://www.docker.com" target="_blank">
        <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
      </a>
    </td>
  </tr>
</table>

## Configuração do Projeto

### Pré-requisitos

- Java 17
- Docker

### Passos para Executar

####  Clone o repositório:
```bash
git clone https://github.com/lgjohann/PbNovDes3_GuilhermeJohann.git
```

#### Baixando e executando os containers:
Na pasta raiz do repositório clonado, execute o seguinte script no terminal:
```bash
docker compose up --build --detach
```

**Por padrão**:

- Microsserviço de eventos está na porta 8080
- Microsserviço de ingressos está na porta 8081

## Documentação Swagger
Caso preferir, a documentação do Swagger dos endpoints de cada microserviço também estão disponíveis nos endereços abaixo:
### ms-event-manager
- http://3.145.162.214:8080/swagger-ui/index.html

### ms-ticket-manager
- http://18.219.97.187:8080/swagger-ui/index.html

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
- `id` (ID do evento)

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
- `id` (ID do evento)

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
- `id` (ID do evento)

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


---

# Microsserviço de Gerenciamento de Ingressos


Microsserviço RESTful para gestão de ingressos com integração de eventos.

---

## 📚 Documentação dos Endpoints

### 🎟️ **Ingressos**

#### **Criar Ingresso**

```http
POST /api/v1/tickets/create-ticket
```
_Cria um novo ingresso vinculado a um evento_

**Corpo da Requisição:**
```json
{
  "customerName": "João Silva",
  "cpf": "12345678909",
  "customerMail": "joao@email.com",
  "eventId": "550e8400e29b41d4a7160",
  "eventName": "Show Internacional",
  "brlAmount": 250.00,
  "usdAmount": 50.00
}
```

**Respostas:**
| Código | Descrição                         |
|--------|-----------------------------------|
| 201    | Ingresso criado com sucesso       |
| 404    | Evento não encontrado             |
| 422    | Dados inválidos ou formato errado |

---

#### **Atualizar Ingresso**
```http
PUT /api/v1/tickets/update-ticket/{id}
```
_Atualiza informações de um ingresso existente_

**Parâmetro:**
- `id` (Número inteiro)

**Corpo da Requisição:**
```json
{
  "customerName": "Novo Nome",
  "customerMail": "novo@email.com",
  "eventId": "novo-event-id"
}
```

**Respostas:**
| Código | Descrição                         |
|--------|-----------------------------------|
| 200    | Ingresso atualizado com sucesso   |
| 404    | Ingresso/Evento não encontrado    |
| 422    | Dados inválidos                   |

---

#### **Buscar Ingresso por ID**
```http
GET /api/v1/tickets/get-ticket/{id}
```
_Recupera detalhes de um ingresso específico_

**Parâmetro:**
- `id` (Número inteiro)

**Respostas:**
| Código | Descrição               |
|--------|-------------------------|
| 200    | Detalhes do ingresso    |
| 404    | Ingresso não encontrado |

---

#### **Buscar Ingressos por CPF**
```http
GET /api/v1/tickets/get-ticket-by-cpf/{cpf}
```
_Lista todos os ingressos de um cliente_

**Parâmetro:**
- `cpf` (String - 11 dígitos)

**Respostas:**
| Código | Descrição                     |
|--------|-------------------------------|
| 200    | Lista de ingressos            |
| 404    | Nenhum ingresso encontrado    |

---

#### **Verificar Ingressos por Evento**
```http
GET /api/v1/tickets/check-tickets-by-event/{eventId}
```
_Lista ingressos vinculados a um evento_

**Parâmetro:**
- `eventId` (ID do evento)

**Respostas:**
| Código | Descrição                     |
|--------|-------------------------------|
| 200    | Lista de ingressos            |
| 404    | Nenhum ingresso encontrado    |

---

#### **Cancelar Ingresso por ID**
```http
DELETE /api/v1/tickets/cancel-ticket/{id}
```
_Cancela um ingresso específico_

**Parâmetro:**
- `id` (Número inteiro)

**Respostas:**
| Código | Descrição                          |
|--------|------------------------------------|
| 204    | Cancelamento bem-sucedido          |
| 404    | Ingresso não encontrado            |
| 409    | Ingresso já cancelado              |

---

#### **Cancelar Ingressos por CPF**
```http
DELETE /api/v1/tickets/cancel-ticket-by-cpf/{cpf}
```
_Cancela todos os ingressos de um cliente_

**Parâmetro:**
- `cpf` (String - 11 dígitos)

**Respostas:**
| Código | Descrição                          |
|--------|------------------------------------|
| 204    | Cancelamento em massa bem-sucedido |
| 404    | Nenhum ingresso encontrado         |
| 409    | Todos ingressos já cancelados      |

---

## 📦 Estruturas de Dados

### **Entrada (TicketCreateDto)**
```json
{
  "customerName": "string (obrigatório)",
  "cpf": "string",
  "customerMail": "string (obrigatório)",
  "eventId": "string (obrigatório)",
  "eventName": "string (obrigatório)",
  "brlAmount": "number (≥ 0)",
  "usdAmount": "number (≥ 0)"
}
```

### **Resposta (TicketResponseDto)**
```json
{
  "ticketId": "integer",
  "cpf": "string",
  "customerName": "string",
  "customerMail": "string",
  "event": {
    "eventId": "string",
    "eventName": "string",
    "eventDateTime": "string",
    "logradouro": "string",
    "bairro": "string",
    "cidade": "string",
    "uf": "string"
  },
  "brlTotalAmount": "number",
  "usdTotalAmount": "number",
  "status": "string"
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

## ⚠️ Requisitos e Validações

1. **Campos Obrigatórios:**
   - `customerName`
   - `customerMail`
   - `eventId`
   - `eventName`

2. **Restrições Numéricas:**
   - `brlAmount` e `usdAmount` devem ser ≥ 0

3. **Formato Especial:**
   - `YYYY-MM-DDTHH:mm:ss` (Ex: `2024-12-31T23:59:59`)
   - `cpf`: 11 dígitos (sem formatação)

---

## 🌐 Endereço Base
`http://18.219.97.187:8080`

---

# Imagens do projeto

## Swagger do ms-event-manager
![swagger-event-aws](https://github.com/user-attachments/assets/3d1971ce-60e1-4a4d-8f9e-38c48ba91a04)

## Coverage de testes do ms-event-manager
![coverage-testes-events](https://github.com/user-attachments/assets/40d93f39-9a8e-4c3e-92df-090e80dfacd5)

## Swagger do ms-ticket-manager
![swagger-ticket-aws](https://github.com/user-attachments/assets/69ef3edd-8881-4e59-9a12-3d9941ba6bfa)

## Coverage de testes do ms-ticket-manager
![coverage-testes-tickets](https://github.com/user-attachments/assets/86410b2d-22bc-41fe-b1de-ac7e760960fe)

---
