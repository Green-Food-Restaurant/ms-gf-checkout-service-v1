package com.greenfood.checkout_service.infrastructure.exceptions.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Representa a resposta de erro padrão da API, seguindo o padrão Result
 */
@Data
public class ApiErrorResponse {
    private int statusCode;
    private String message;
    private List<String> errors;
    private Map<String, List<String>> validationErrors;
    private LocalDateTime timestamp;
    
    public ApiErrorResponse(int statusCode, String message, List<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiErrorResponse(int statusCode, String message, List<String> errors, Map<String, List<String>> validationErrors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
        this.validationErrors = validationErrors;
        this.timestamp = LocalDateTime.now();
    }
}
