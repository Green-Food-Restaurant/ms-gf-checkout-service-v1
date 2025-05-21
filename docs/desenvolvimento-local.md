# Guia de Desenvolvimento Local

Este guia fornece instruções para configurar e executar o microsserviço de checkout localmente para desenvolvimento e testes.

## Pré-requisitos

- Java 21
- Maven 3.8+
- Docker e Docker Compose
- Postman ou similar (para testes de API)
- MongoDB (local ou via Docker)
- Kafka (local ou via Docker)

## Configuração do Ambiente

### 1. Clone o Repositório

```bash
git clone https://github.com/greenfood/ms-gf-checkout-service-v1.git
cd ms-gf-checkout-service-v1
```

### 2. Configuração do MongoDB e Kafka via Docker

O projeto inclui um arquivo `docker-compose.yml` para facilitar a configuração das dependências:

```bash
cd docker
docker-compose up -d
```

Este comando iniciará:
- MongoDB na porta 27017
- Zookeeper na porta 2181
- Kafka na porta 9092

### 3. Configuração de Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```
MONGODB_URI=mongodb://localhost:27017/greenfood-checkout-service
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
MERCADOPAGO_ACCESS_TOKEN=APP_USR-7543957714286812-020420-f15fc776d1a6a5e15205755eb8a707c0-2231192113
JWT_SECRET=secret-local-development
```

> **Nota**: Para desenvolvimento local, você pode usar o token de sandbox do Mercado Pago. Para produção, use um token real.

### 4. Compilação do Projeto

```bash
mvn clean install
```

### 5. Execução do Projeto

```bash
mvn spring-boot:run
```

O serviço estará disponível em: http://localhost:8090

## Estrutura de Diretórios

- `src/main/java/com/greenfood/checkout_service/`
  - `application/`: Casos de uso e portas
  - `domain/`: Modelos e regras de negócio
  - `infrastructure/`: Adaptadores e configurações
- `src/main/resources/`: Arquivos de configuração
- `src/test/`: Testes automatizados
- `docker/`: Arquivos para Docker
- `docs/`: Documentação adicional

## Testando a API

### Endpoint de Checkout

#### Request

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

#### Response (Sucesso)

```json
{
  "statusCode": 200,
  "message": "Checkout processado com sucesso",
  "data": {
    "paymentId": "1234567890",
    "status": "pending",
    "paymentUrl": "https://www.mercadopago.com.br/checkout/v1/redirect?pref_id=1234567890",
    "qrCode": "data:image/png;base64,..."
  },
  "timestamp": "2025-05-21T15:30:45.123Z"
}
```

## Depuração

### Logs

Os logs são armazenados em:
- `logs/spring-boot-logger.log`

Você pode alterar o nível de log no arquivo `src/main/resources/logback-spring.xml`

### Monitoramento do MongoDB

Para verificar os documentos salvos no MongoDB:

```bash
docker exec -it ms-gf-checkout-service-v1_mongodb_1 mongosh
use greenfood-checkout-service
db.payment.find()
```

### Monitoramento do Kafka

Para verificar as mensagens publicadas no Kafka:

```bash
docker exec -it ms-gf-checkout-service-v1_kafka_1 kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic payment-topic --from-beginning
```

## Solução de Problemas Comuns

### Porta 8090 já em uso

```bash
netstat -ano | findstr :8090
taskkill /PID <PID> /F
```

Ou altere a porta no arquivo `application.yaml`:

```yaml
server:
  port: 8091
```

### Erro de conexão com MongoDB

Verifique se o MongoDB está em execução:

```bash
docker ps | grep mongo
```

Se não estiver, inicie-o:

```bash
docker start ms-gf-checkout-service-v1_mongodb_1
```

### Erro de conexão com Kafka

Verifique se o Kafka está em execução:

```bash
docker ps | grep kafka
```

Se não estiver, inicie-o:

```bash
docker start ms-gf-checkout-service-v1_kafka_1
```

## Convenções de Código

- Use o estilo de código padrão Java
- Adicione comentários em métodos complexos
- Mantenha os commits atômicos e descritivos
- Siga a arquitetura hexagonal existente

## Fluxo de Trabalho de Desenvolvimento

1. Crie uma branch a partir de `develop`
2. Implemente as alterações
3. Execute os testes: `mvn test`
4. Faça commit das alterações
5. Crie um PR para `develop`

## Recursos Adicionais

- [Documentação do Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Documentação do MongoDB](https://docs.mongodb.com/)
- [Documentação do Kafka](https://kafka.apache.org/documentation/)
- [Documentação do Mercado Pago](https://www.mercadopago.com.br/developers/pt/docs/checkout-api/landing)
- [Documentação do Padrão Result](./result-pattern.md)
- [Documentação de Autenticação](./api-gateway-auth.md)
