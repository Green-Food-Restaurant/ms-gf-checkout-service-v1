package com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions;

public class InvalidJsonFormatException extends RuntimeException {
    public InvalidJsonFormatException(String message) {
        super(message);
    }
} 