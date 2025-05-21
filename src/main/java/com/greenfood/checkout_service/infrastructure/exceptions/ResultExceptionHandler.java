package com.greenfood.checkout_service.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.greenfood.checkout_service.domain.result.Result;
import com.greenfood.checkout_service.domain.result.ValidationResult;
import com.greenfood.checkout_service.infrastructure.exceptions.response.ApiErrorResponse;
import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.InvalidJsonFormatException;
import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.MissingParameterException;
import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.PaymentProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ResultExceptionHandler {

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentProcessingException(PaymentProcessingException ex) {
        Result result = Result.fail(ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Erro no processamento do pagamento",
            result.getErrors()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJsonFormatException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJsonFormat(InvalidJsonFormatException ex) {
        Result result = Result.fail(ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Formato de JSON inválido",
            result.getErrors()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParameter(MissingParameterException ex) {
        Result result = Result.fail(ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Parâmetro obrigatório ausente",
            result.getErrors()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Result result = Result.fail("O JSON enviado está em formato inválido ou com campos incorretos");
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "JSON inválido",
            result.getErrors()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ValidationResult validationResult = ValidationResult.ok();
        
        // Agrupamos os erros por campo
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationResult.addError(error.getField(), error.getDefaultMessage());
        }
        
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Erros de validação encontrados",
            validationResult.getErrors(),
            validationResult.getValidationErrors()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        Result result = Result.fail("O parâmetro '" + ex.getParameterName() + "' é obrigatório");
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Parâmetro obrigatório ausente",
            result.getErrors()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        log.error("Erro não tratado: ", ex);
        Result result = Result.fail("Ocorreu um erro interno no servidor");
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno do servidor",
            result.getErrors()
        );
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
