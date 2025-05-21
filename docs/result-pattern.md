# Documentação Técnica: Padrão Result

## Visão Geral

O padrão Result implementado neste projeto é uma abordagem para gerenciar o resultado de operações que podem falhar, evitando o uso excessivo de exceções para controle de fluxo. Este documento descreve a implementação e uso do padrão.

## Classes Principais

### Result

A classe base que representa o resultado de uma operação:

```java
public class Result {
    private boolean success;
    private List<String> errors;
    
    // Métodos de criação
    public static Result ok() { ... }
    public static Result fail(String error) { ... }
    public static Result fail(List<String> errors) { ... }
    
    // Métodos de consulta
    public boolean isSuccess() { ... }
    public boolean isFailure() { ... }
    public List<String> getErrors() { ... }
}
```

### ResultT<T>

Versão genérica que pode transportar dados junto com o status:

```java
public class ResultT<T> extends Result {
    private T data;
    
    // Métodos de criação
    public static <T> ResultT<T> ok(T data) { ... }
    public static <T> ResultT<T> failWithError(String error) { ... }
    public static <T> ResultT<T> failWithErrors(List<String> errors) { ... }
    
    // Método de acesso ao dado
    public T getData() { ... }
}
```

### ValidationResult

Especialização para validações de campos:

```java
public class ValidationResult extends Result {
    private Map<String, List<String>> validationErrors;
    
    // Métodos de criação
    public static ValidationResult ok() { ... }
    public static ValidationResult fail(Map<String, List<String>> validationErrors) { ... }
    
    // Métodos de manipulação
    public ValidationResult addError(String field, String errorMessage) { ... }
}
```

## Respostas HTTP Padronizadas

### Sucesso (ApiSuccessResponse)

```json
{
  "statusCode": 200,
  "message": "Operação realizada com sucesso",
  "data": { ... },
  "timestamp": "2025-05-21T10:15:30.123Z"
}
```

### Erro (ApiErrorResponse)

```json
{
  "statusCode": 400,
  "message": "Erro na operação",
  "errors": ["Descrição do erro"],
  "validationErrors": {
    "campo1": ["Erro de validação 1", "Erro de validação 2"],
    "campo2": ["Erro de validação 3"]
  },
  "timestamp": "2025-05-21T10:15:30.123Z"
}
```

## Uso na Camada de Controller

```java
@PostMapping("/endpoint")
public ResponseEntity<?> execute(@Valid @RequestBody RequestDto request) {
    ResultT<ResponseDto> result = useCase.execute(request);
    return ApiResponseBuilder.fromResult(result, "Operação bem-sucedida");
}
```

## Uso na Camada de Caso de Uso

```java
@Override
public ResultT<ResponseDto> execute(RequestDto request) {
    try {
        // Lógica de negócio
        ResponseDto response = processRequest(request);
        return ResultT.ok(response);
    } catch (DomainException e) {
        return ResultT.<ResponseDto>failWithError(e.getMessage());
    } catch (Exception e) {
        return ResultT.<ResponseDto>failWithError("Erro inesperado ao processar requisição");
    }
}
```

## Benefícios do Padrão Result

1. **Controle de Fluxo Explícito:** Torna explícita a possibilidade de falha
2. **Redução de Exceções:** Evita uso de exceções para controle de fluxo
3. **Respostas Padronizadas:** Fornece formato consistente de resposta para sucesso e erro
4. **Validações Detalhadas:** Permite retornar informações detalhadas sobre validações
5. **Legibilidade:** Torna o código mais legível e a intenção mais clara

## Considerações

- Use `Result` para operações simples sem retorno de dados
- Use `ResultT<T>` para operações que retornam dados em caso de sucesso
- Use `ValidationResult` para operações que envolvem validações de múltiplos campos
- Use `ApiResponseBuilder` para transformar Results em ResponseEntity

## Exemplo Completo

```java
// Controller
@PostMapping("/checkout")
public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request) {
    ResultT<CheckoutResponse> result = checkoutUseCase.execute(request);
    return ApiResponseBuilder.fromResult(result, "Checkout realizado com sucesso");
}

// Use Case
public ResultT<CheckoutResponse> execute(CheckoutRequest request) {
    try {
        // Validação
        ValidationResult validationResult = validate(request);
        if (validationResult.isFailure()) {
            return ResultT.<CheckoutResponse>failWithErrors(validationResult.getErrors());
        }
        
        // Processamento
        Payment payment = processPayment(request);
        
        // Retorno
        CheckoutResponse response = mapToResponse(payment);
        return ResultT.ok(response);
    } catch (PaymentException e) {
        return ResultT.<CheckoutResponse>failWithError("Erro no processamento do pagamento: " + e.getMessage());
    } catch (Exception e) {
        logger.error("Erro inesperado", e);
        return ResultT.<CheckoutResponse>failWithError("Ocorreu um erro inesperado");
    }
}
```
