package com.greenfood.checkout_service.domain.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * Versão especializada do Result para tratamento de validações,
 * permitindo associar erros a campos específicos.
 */
@Getter
public class ValidationResult extends Result {
    private Map<String, List<String>> validationErrors;
    
    private ValidationResult() {
        super();
        this.validationErrors = new HashMap<>();
    }
    
    private ValidationResult(Map<String, List<String>> validationErrors) {
        super(flattenValidationErrors(validationErrors));
        this.validationErrors = validationErrors != null ? validationErrors : new HashMap<>();
    }
    
    /**
     * Cria um resultado de validação bem-sucedido.
     */
    public static ValidationResult ok() {
        return new ValidationResult();
    }
    
    /**
     * Cria um resultado de validação com falha, com erros associados a campos.
     */
    public static ValidationResult fail(Map<String, List<String>> validationErrors) {
        return new ValidationResult(validationErrors);
    }
    
    /**
     * Adiciona um erro de validação para um campo específico.
     */
    public ValidationResult addError(String field, String errorMessage) {
        if (!validationErrors.containsKey(field)) {
            validationErrors.put(field, new ArrayList<>());
        }
        validationErrors.get(field).add(errorMessage);
        return this;
    }
    
    /**
     * Verifica se o resultado tem erros de validação.
     */
    @Override
    public boolean isSuccess() {
        return validationErrors.isEmpty();
    }
    
    /**
     * Converte os erros de validação em uma lista plana de mensagens.
     */
    private static List<String> flattenValidationErrors(Map<String, List<String>> validationErrors) {
        if (validationErrors == null) {
            return new ArrayList<>();
        }
        
        List<String> errors = new ArrayList<>();
        validationErrors.forEach((field, fieldErrors) -> {
            fieldErrors.forEach(error -> errors.add(field + ": " + error));
        });
        
        return errors;
    }
}
