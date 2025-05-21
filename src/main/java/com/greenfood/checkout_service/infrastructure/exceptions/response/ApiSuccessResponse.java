package com.greenfood.checkout_service.infrastructure.exceptions.response;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Representa a resposta de sucesso padrão da API, seguindo o padrão Result
 */
@Data
public class ApiSuccessResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    
    public ApiSuccessResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
