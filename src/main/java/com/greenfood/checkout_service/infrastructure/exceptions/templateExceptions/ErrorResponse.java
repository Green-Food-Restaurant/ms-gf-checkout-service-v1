package com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
    private int statusCode;
    private String statusMessage;
    private List<String> errors;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int statusCode, String statusMessage, String error, String message) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.errors = List.of(error);
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int statusCode, String statusMessage, List<String> errors, String message) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.errors = errors;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
