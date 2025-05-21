package com.greenfood.checkout_service.infrastructure.exceptions.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.greenfood.checkout_service.domain.result.Result;
import com.greenfood.checkout_service.domain.result.ResultT;

/**
 * Fornece métodos auxiliares para criar respostas padronizadas a partir de objetos Result
 */
public class ApiResponseBuilder {

    /**
     * Cria uma resposta HTTP a partir de um objeto Result.
     * 
     * @param <T> O tipo de dados no objeto ResultT
     * @param result O objeto ResultT que contém os dados e o status
     * @param successStatus O status HTTP a ser usado em caso de sucesso
     * @param errorStatus O status HTTP a ser usado em caso de erro
     * @param successMessage A mensagem a ser incluída em caso de sucesso
     * @return Uma resposta HTTP apropriada com base no resultado
     */
    public static <T> ResponseEntity<?> fromResult(
            ResultT<T> result, 
            HttpStatus successStatus, 
            HttpStatus errorStatus,
            String successMessage) {
        
        if (result.isSuccess()) {
            ApiSuccessResponse<T> response = new ApiSuccessResponse<>(
                successStatus.value(),
                successMessage,
                result.getData()
            );
            return new ResponseEntity<>(response, successStatus);
        } else {
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                errorStatus.value(),
                "Erro na operação",
                result.getErrors()
            );
            return new ResponseEntity<>(errorResponse, errorStatus);
        }
    }
    
    /**
     * Cria uma resposta HTTP a partir de um objeto Result, sem dados.
     * 
     * @param result O objeto Result que contém o status
     * @param successStatus O status HTTP a ser usado em caso de sucesso
     * @param errorStatus O status HTTP a ser usado em caso de erro
     * @param successMessage A mensagem a ser incluída em caso de sucesso
     * @return Uma resposta HTTP apropriada com base no resultado
     */
    public static ResponseEntity<?> fromResult(
            Result result, 
            HttpStatus successStatus, 
            HttpStatus errorStatus,
            String successMessage) {
        
        if (result.isSuccess()) {
            ApiSuccessResponse<String> response = new ApiSuccessResponse<>(
                successStatus.value(),
                successMessage,
                "Operação realizada com sucesso"
            );
            return new ResponseEntity<>(response, successStatus);
        } else {
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                errorStatus.value(),
                "Erro na operação",
                result.getErrors()
            );
            return new ResponseEntity<>(errorResponse, errorStatus);
        }
    }
    
    /**
     * Versão simplificada que usa HTTP 200 para sucesso e HTTP 400 para erros
     */
    public static <T> ResponseEntity<?> fromResult(ResultT<T> result, String successMessage) {
        return fromResult(result, HttpStatus.OK, HttpStatus.BAD_REQUEST, successMessage);
    }
    
    /**
     * Versão simplificada para Result sem dados
     */
    public static ResponseEntity<?> fromResult(Result result, String successMessage) {
        return fromResult(result, HttpStatus.OK, HttpStatus.BAD_REQUEST, successMessage);
    }
}
