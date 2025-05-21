# Autenticação via API Gateway

## Visão Geral

Este microsserviço de checkout utiliza autenticação centralizada via API Gateway, o que permite manter a lógica de autenticação separada da lógica de negócio. Este documento descreve o fluxo de autenticação e como o microsserviço interage com o API Gateway.

## Fluxo de Autenticação

![Fluxo de Autenticação](https://raw.githubusercontent.com/user/repo/main/diagrams/auth-flow.png)

1. O cliente faz uma requisição de autenticação para o API Gateway
2. API Gateway valida as credenciais e gera um token JWT
3. Cliente recebe o token JWT
4. Para acessar o microsserviço, o cliente envia o token no header Authorization
5. API Gateway valida o token e, se válido, encaminha a requisição para o microsserviço
6. O microsserviço processa a requisição sem se preocupar com a autenticação

## Configuração no API Gateway

O API Gateway deve ser configurado para:

1. Gerar tokens JWT com as claims necessárias (usuário, permissões, etc.)
2. Validar tokens em todas as requisições para o microsserviço
3. Encaminhar o token e informações relevantes para o microsserviço

## Configuração no Microsserviço

No arquivo `application.yaml` já existe a configuração básica para referência da chave JWT:

```yaml
security:
  jwt:
    secret: ${JWT_SECRET:secret}
    token-prefix: Bearer
```

Esta configuração é usada apenas para validação de tokens em casos excepcionais, já que a autenticação principal é feita no API Gateway.

## Headers Relevantes

O API Gateway envia as seguintes informações via headers HTTP:

| Header | Descrição |
|--------|-----------|
| `Authorization` | Token JWT com prefixo Bearer |
| `X-User-Id` | ID do usuário autenticado |
| `X-User-Role` | Papel/perfil do usuário |

## Benefícios da Abordagem

1. **Separação de Responsabilidades**: O microsserviço não precisa se preocupar com autenticação
2. **Reutilização**: A lógica de autenticação é implementada uma vez no API Gateway
3. **Consistência**: Todos os microsserviços usam a mesma abordagem de segurança
4. **Facilidade de Manutenção**: Alterações na lógica de autenticação são feitas em um único lugar

## Considerações de Segurança

1. A comunicação entre o API Gateway e o microsserviço deve ser segura (HTTPS/TLS)
2. O microsserviço deve ser configurado para aceitar requisições apenas do API Gateway
3. Em ambiente de produção, o segredo JWT deve ser armazenado de forma segura
4. Tokens devem ter tempo de expiração adequado

## Exemplo de Integração

Em caso de necessidade de acesso às informações do usuário dentro do microsserviço, pode-se criar um serviço que extraia as informações dos headers:

```java
@Service
public class UserContextService {
    
    private final HttpServletRequest request;
    
    public UserContextService(HttpServletRequest request) {
        this.request = request;
    }
    
    public String getCurrentUserId() {
        return request.getHeader("X-User-Id");
    }
    
    public String getCurrentUserRole() {
        return request.getHeader("X-User-Role");
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(getCurrentUserRole());
    }
}
```

Este serviço pode ser injetado em controllers ou services para obter informações do usuário autenticado.

## Testes

Para testar o microsserviço localmente sem o API Gateway, pode-se simular os headers de autenticação:

```bash
curl -X POST http://localhost:8090/v1/checkout/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c" \
  -H "X-User-Id: 123" \
  -H "X-User-Role: CUSTOMER" \
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
