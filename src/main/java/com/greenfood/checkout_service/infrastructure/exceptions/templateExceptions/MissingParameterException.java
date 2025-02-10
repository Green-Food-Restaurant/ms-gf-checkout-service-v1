package com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions;

public class MissingParameterException extends RuntimeException {
    public MissingParameterException(String message) {
        super(message);
    }
} 