package com.greenfood.checkout_service.domain.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

/**
 * Classe base para o padrão Result, que encapsula o resultado de uma operação
 * que pode falhar, eliminando a necessidade de lançar exceções.
 */
@Getter
public class Result {
    protected boolean success;
    protected List<String> errors;
    
    protected Result() {
        this.success = true;
        this.errors = new ArrayList<>();
    }
    
    protected Result(List<String> errors) {
        this.success = false;
        this.errors = errors != null ? errors : new ArrayList<>();
    }
    
    protected Result(String error) {
        this.success = false;
        this.errors = new ArrayList<>();
        if (error != null) {
            this.errors.add(error);
        }
    }
    
    /**
     * Cria um resultado de sucesso.
     */
    public static Result ok() {
        return new Result();
    }
    
    /**
     * Cria um resultado de falha com uma lista de erros.
     */
    public static Result fail(List<String> errors) {
        return new Result(errors);
    }
    
    /**
     * Cria um resultado de falha com uma mensagem de erro.
     */
    public static Result fail(String error) {
        return new Result(error);
    }
    
    /**
     * Verifica se o resultado foi bem-sucedido.
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Verifica se o resultado falhou.
     */
    public boolean isFailure() {
        return !success;
    }
    
    /**
     * Retorna a lista imutável de erros.
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
