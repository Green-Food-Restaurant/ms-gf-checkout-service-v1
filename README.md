# Microsserviço de Checkout - Green Food

Este microsserviço é responsável pelo processamento de pagamentos e checkout para a plataforma Green Food, integrando com o gateway de pagamento do Mercado Pago.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Configuração](#configuração)
- [Uso](#uso)
- [API](#api)
- [Padrões Implementados](#padrões-implementados)
- [CI/CD e Deploy](#cicd-e-deploy)
- [Desenvolvimento](#desenvolvimento)

## 🔍 Visão Geral

O Microsserviço de Checkout é responsável por processar pagamentos dos pedidos feitos na plataforma Green Food, gerenciando as transações e integrando com o gateway de pagamento do Mercado Pago. Ele utiliza uma arquitetura hexagonal para garantir a separação de responsabilidades e facilitar a manutenção e evolução do sistema.

## 💻 Tecnologias

- **Java 21**
- **Spring Boot 3.4.1**
- **MongoDB** - Para persistência de dados
- **Kafka** - Para comunicação entre serviços
- **Mercado Pago SDK** - Para integração com o gateway de pagamento
- **Maven** - Para gerenciamento de dependências
- **Docker** - Para conteinerização

## 🏛️ Arquitetura

O projeto segue a arquitetura hexagonal (também conhecida como Ports and Adapters), que permite uma clara separação entre a lógica de negócio e as integrações externas:

- **Domain** - Contém as entidades e regras de negócio
- **Application** - Contém os casos de uso e portas de entrada/saída
- **Infrastructure** - Contém adaptadores para tecnologias externas

## ✨ Funcionalidades

- Processamento de pagamentos via Mercado Pago
- Persistência do histórico de transações
- Tratamento de erros estruturado usando o padrão Result
- Comunicação assíncrona via Kafka

## ⚙️ Configuração

### Pré-requisitos

- Java 21
- Docker e Docker Compose
- Maven

### Variáveis de Ambiente

```env
MONGODB_URI=mongodb://localhost:27017/greenfood-checkout-service
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
MERCADOPAGO_ACCESS_TOKEN=APP_USR-xxxxxxxx
JWT_SECRET=seu-secret-aqui
```

### Executando com Docker

```bash
cd docker
docker-compose up -d
```

### Executando localmente

```bash
mvn spring-boot:run
```

## 🚀 Uso

O serviço pode ser usado através de requisições HTTP para o endpoint de checkout:

```bash
curl -X POST http://localhost:8090/v1/checkout/create \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "123456",
    "amount": 150.00,
    "customerInfo": {
      "name": "Nome do Cliente",
      "email": "cliente@email.com"
    },
    "items": [
      {
        "description": "Produto 1",
        "quantity": 2,
        "unitPrice": 75.00
      }
    ]
  }'
```

## 📡 API

### Endpoints

#### POST /v1/checkout/create

Processa um novo checkout e pagamento.

**Request:**
```json
{
  "orderId": "string",
  "amount": 0.0,
  "customerInfo": {
    "name": "string",
    "email": "string"
  },
  "items": [
    {
      "description": "string",
      "quantity": 0,
      "unitPrice": 0.0
    }
  ]
}
```

**Resposta de Sucesso:**
```json
{
  "statusCode": 200,
  "message": "Checkout processado com sucesso",
  "data": {
    "paymentId": "string",
    "status": "string",
    "paymentUrl": "string",
    "qrCode": "string"
  },
  "timestamp": "2025-05-21T15:30:45.123Z"
}
```

**Resposta de Erro:**
```json
{
  "statusCode": 400,
  "message": "Erro na operação",
  "errors": ["Descrição do erro"],
  "timestamp": "2025-05-21T15:30:45.123Z"
}
```

## 🧩 Padrões Implementados

### Padrão Result

O projeto utiliza o padrão Result para tratamento de exceções e resultados de operações. Este padrão evita o uso de exceções para controle de fluxo e proporciona respostas mais estruturadas.

Principais classes:
- `Result` - Representa o resultado de uma operação (sucesso ou falha)
- `ResultT<T>` - Versão genérica que pode conter dados
- `ValidationResult` - Especializado para validações de campos

### Arquitetura Hexagonal

A utilização do padrão de portas e adaptadores permite:
- Isolamento da lógica de negócio
- Fácil substituição de componentes externos
- Testabilidade aprimorada

## 🚢 CI/CD e Deploy

O projeto utiliza GitHub Actions para automação do processo de CI/CD e Coolify para deploy contínuo.

### Pipeline de CI/CD

[![Build, Push e Deploy](https://github.com/Green-Food-Restaurant/ms-gf-checkout-service-v1/actions/workflows/build.yml/badge.svg)](https://github.com/Green-Food-Restaurant/ms-gf-checkout-service-v1/actions/workflows/build.yml)

O pipeline inclui as seguintes etapas:
- Compilação e testes unitários
- Análise de vulnerabilidades com Trivy
- Construção e publicação de imagem Docker
- Deploy automatizado no Coolify

### Deploy com Coolify

O serviço é hospedado utilizando a plataforma Coolify, que oferece:
- Deploy automatizado a partir do GitHub Actions
- Monitoramento de recursos e logs
- Escalonamento automático
- Gerenciamento de domínios e TLS

Para mais informações:
- [Pipeline de Build e Segurança](docs/pipeline-build-seguranca.md)
- [Documentação do Coolify](docs/deploy-coolify.md)
- [Guia de Configuração do Coolify](docs/configuracao-coolify.md)

## 👨‍💻 Desenvolvimento

### Estrutura do projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── greenfood/
│   │           └── checkout_service/
│   │               ├── application/
│   │               │   ├── dtos/
│   │               │   ├── port/
│   │               │   │   ├── in/
│   │               │   │   └── out/
│   │               │   └── useCases/
│   │               ├── domain/
│   │               │   ├── enums/
│   │               │   ├── model/
│   │               │   └── result/
│   │               └── infrastructure/
│   │                   ├── adapter/
│   │                   │   ├── in/
│   │                   │   │   └── controller/
│   │                   │   └── out/
│   │                   ├── configuration/
│   │                   ├── exceptions/
│   │                   └── mapper/
│   └── resources/
│       └── application.yaml
└── test/
    └── java/
        └── com/
            └── greenfood/
                └── checkout_service/
```

### Autenticação

O serviço está configurado para utilizar autenticação via API Gateway, que fornece um token JWT para validação. A segurança é implementada no gateway, simplificando a lógica do microsserviço.

---

## 🔒 Segurança

As credenciais de acesso ao Mercado Pago são gerenciadas via variáveis de ambiente para evitar exposição no código fonte.

---

Desenvolvido com ❤️ pela equipe Green Food.
