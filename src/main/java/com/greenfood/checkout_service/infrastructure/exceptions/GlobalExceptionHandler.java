package com.greenfood.checkout_service.infrastructure.exceptions;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.ErrorResponse;

import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.InvalidJsonFormatException;
import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.MissingParameterException;
import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.PaymentProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ErrorResponse> handlePaymentProcessingException(PaymentProcessingException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Erro no processamento do pagamento", ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );

    }


    @ExceptionHandler(InvalidJsonFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJsonFormat(InvalidJsonFormatException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Formato de JSON inválido", ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );

    }


    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingParameterException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Parâmetro obrigatório ausente", ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );

    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "JSON inválido", "O JSON enviado está em formato inválido ou com campos incorretos"),
            HttpStatus.BAD_REQUEST
        );


    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
            
        return new ResponseEntity<>(
            new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors,
                "Erros de validação encontrados"
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Parâmetro obrigatório ausente", "O parâmetro '" + ex.getParameterName() + "' é obrigatório"),
            HttpStatus.BAD_REQUEST
        );

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Erro interno do servidor", ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

