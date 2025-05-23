# Guia de Implementação do Padrão Result

Este guia oferece orientações práticas para implementar o padrão Result de forma consistente no microsserviço de checkout da Green Food.

## 1. Interfaces e Portas

Ao definir uma interface (porta) para um caso de uso ou adaptador, use `ResultT<T>` como tipo de retorno quando a operação retornar dados:

```java
public interface MinhaPortaIn {
    ResultT<MinhaResponseDto> execute(MinhaRequestDto requestDto);
}

public interface MinhaPortaOut {
    ResultT<DadosExternos> buscarDados(ParametrosBusca parametros);
}
```

## 2. Casos de Uso

Na implementação de casos de uso:

```java
@Override
public ResultT<MinhaResponseDto> execute(MinhaRequestDto requestDto) {
    // 1. Validação
    ValidationResult validationResult = validar(requestDto);
    if (validationResult.isFailure()) {
        return ResultT.<MinhaResponseDto>failWithErrors(validationResult.getErrors());
    }
    
    try {
        // 2. Lógica de negócio
        Entidade entidade = processarRequest(requestDto);
        
        // 3. Chamada a adaptadores externos, capturando e propagando falhas
        ResultT<DadosExternos> resultadoExterno = portaExternaOut.buscarDados(parametros);
        if (resultadoExterno.isFailure()) {
            return ResultT.<MinhaResponseDto>failWithErrors(resultadoExterno.getErrors());
        }
        
        // 4. Processamento adicional e criação da resposta
        MinhaResponseDto response = mapearParaResponse(entidade, resultadoExterno.getData());
        return ResultT.ok(response);
    } catch (Exception e) {
        // 5. Tratamento de exceções não esperadas
        log.error("Erro inesperado ao processar requisição: {}", e.getMessage(), e);
        return ResultT.<MinhaResponseDto>failWithError("Erro ao processar a requisição: " + e.getMessage());
    }
}
```

## 3. Adaptadores de Entrada (Controllers)

Nos controladores REST:

```java
@PostMapping("/endpoint")
public ResponseEntity<?> executarOperacao(@Valid @RequestBody MinhaRequestDto requestDto) {
    log.info("Iniciando operação com dados: {}", requestDto);
    
    ResultT<MinhaResponseDto> result = casoDeUso.execute(requestDto);
    
    if (result.isSuccess()) {
        log.info("Operação concluída com sucesso");
    } else {
        log.error("Falha na operação: {}", result.getErrors());
    }
    
    return ApiResponseBuilder.fromResult(result, "Operação realizada com sucesso");
}
```

## 4. Adaptadores de Saída (Gateways, Repositories)

Para adaptadores que se comunicam com sistemas externos:

```java
@Override
public ResultT<DadosExternos> buscarDados(ParametrosBusca parametros) {
    try {
        // Comunicação com sistema externo
        RestResponse response = clienteRest.get(parametros);
        
        // Verificação de erros da API externa
        if (!response.isSuccessful()) {
            log.error("Erro na API externa: {}", response.errorBody());
            return ResultT.failWithError("Erro ao comunicar com sistema externo: " + response.errorMessage());
        }
        
        // Processamento da resposta
        DadosExternos dados = converterResposta(response.body());
        return ResultT.ok(dados);
    } catch (TimeoutException e) {
        log.error("Timeout ao comunicar com sistema externo: {}", e.getMessage());
        return ResultT.failWithError("Timeout ao comunicar com sistema externo");
    } catch (Exception e) {
        log.error("Erro ao buscar dados externos: {}", e.getMessage(), e);
        return ResultT.failWithError("Erro ao buscar dados: " + e.getMessage());
    }
}
```

## 5. Validação

Para implementar validações:

```java
private ValidationResult validar(MinhaRequestDto request) {
    ValidationResult result = ValidationResult.ok();
    
    if (request.getValor() == null) {
        result.addError("valor", "Valor não pode ser nulo");
    } else if (request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
        result.addError("valor", "Valor deve ser maior que zero");
    }
    
    if (request.getDescricao() == null || request.getDescricao().trim().isEmpty()) {
        result.addError("descricao", "Descrição não pode ser vazia");
    }
    
    return result;
}
```

## 6. Testes

Ao testar componentes que utilizam o padrão Result:

```java
@Test
void deveRetornarSucessoQuandoOperacaoForValida() {
    // Arrange
    MinhaRequestDto request = criarRequestValido();
    
    // Act
    ResultT<MinhaResponseDto> result = casoDeUso.execute(request);
    
    // Assert
    assertTrue(result.isSuccess());
    assertNotNull(result.getData());
    assertEquals(valorEsperado, result.getData().getValor());
}

@Test
void deveRetornarFalhaQuandoOperacaoForInvalida() {
    // Arrange
    MinhaRequestDto request = criarRequestInvalido();
    
    // Act
    ResultT<MinhaResponseDto> result = casoDeUso.execute(request);
    
    // Assert
    assertTrue(result.isFailure());
    assertFalse(result.getErrors().isEmpty());
    assertTrue(result.getErrors().get(0).contains("mensagem esperada"));
}
```

## 7. Boas Práticas

1. **Seja Específico nos Erros**: Forneça mensagens claras que identifiquem o problema.
2. **Evite Null**: Não retorne null como dados em resultados de sucesso.
3. **Logging Consistente**: Sempre registre erros com contexto suficiente.
4. **Propagação de Erros**: Quando apropriado, propague erros de camadas inferiores em vez de criar novos.
5. **Não Ignore Falhas**: Sempre verifique se um Result é sucesso antes de acessar seus dados.
