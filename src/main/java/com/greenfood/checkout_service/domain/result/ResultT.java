package com.greenfood.checkout_service.domain.result;

import java.util.List;

import lombok.Getter;

/**
 * Versão genérica da classe Result que pode conter uma carga útil (payload).
 * Útil para retornar dados junto com o status de sucesso/falha.
 */
@Getter
public class ResultT<T> extends Result {
    private T data;
    
    private ResultT(T data) {
        super();
        this.data = data;
    }
    
    private ResultT(List<String> errors) {
        super(errors);
        this.data = null;
    }
    
    private ResultT(String error) {
        super(error);
        this.data = null;
    }
    
    /**
     * Cria um resultado de sucesso com dados.
     */
    public static <T> ResultT<T> ok(T data) {
        return new ResultT<>(data);
    }
    
    /**
     * Cria um resultado de falha com uma lista de erros.
     */
    public static <T> ResultT<T> failWithErrors(List<String> errors) {
        return new ResultT<>(errors);
    }
    
    /**
     * Cria um resultado de falha com uma mensagem de erro.
     */
    public static <T> ResultT<T> failWithError(String error) {
        return new ResultT<>(error);
    }
    
    /**
     * Retorna os dados se o resultado for bem-sucedido; caso contrário, retorna null.
     */
    public T getData() {
        return isSuccess() ? data : null;
    }
}
