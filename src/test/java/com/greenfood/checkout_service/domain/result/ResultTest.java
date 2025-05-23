package com.greenfood.checkout_service.domain.result;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ResultTest {

    @Test
    @DisplayName("Result.ok() deve criar um resultado de sucesso")
    void okTest() {
        Result result = Result.ok();
        
        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertTrue(result.getErrors().isEmpty());
    }
    
    @Test
    @DisplayName("Result.fail(String) deve criar um resultado de falha com uma mensagem")
    void failWithStringTest() {
        String errorMessage = "Erro de teste";
        Result result = Result.fail(errorMessage);
        
        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertEquals(1, result.getErrors().size());
        assertEquals(errorMessage, result.getErrors().get(0));
    }
    
    @Test
    @DisplayName("Result.fail(List) deve criar um resultado de falha com múltiplas mensagens")
    void failWithListTest() {
        List<String> errorMessages = Arrays.asList("Erro 1", "Erro 2");
        Result result = Result.fail(errorMessages);
        
        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertEquals(2, result.getErrors().size());
        assertEquals("Erro 1", result.getErrors().get(0));
        assertEquals("Erro 2", result.getErrors().get(1));
    }
    
    @Test
    @DisplayName("getErrors deve retornar uma lista imutável")
    void errorsImmutabilityTest() {
        Result result = Result.fail("Erro de teste");
        
        assertThrows(UnsupportedOperationException.class, () -> {
            result.getErrors().add("Outro erro");
        });
    }
    
    @Test
    @DisplayName("ResultT.ok(data) deve criar um resultado de sucesso com dados")
    void resultTOkTest() {
        String data = "Dados de teste";
        ResultT<String> result = ResultT.ok(data);
        
        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertTrue(result.getErrors().isEmpty());
        assertEquals(data, result.getData());
    }
    
    @Test
    @DisplayName("ResultT.failWithError deve criar um resultado de falha sem dados")
    void resultTFailWithErrorTest() {
        String errorMessage = "Erro de teste";
        ResultT<String> result = ResultT.failWithError(errorMessage);
        
        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertEquals(1, result.getErrors().size());
        assertEquals(errorMessage, result.getErrors().get(0));
        assertNull(result.getData());
    }
    
    @Test
    @DisplayName("ResultT.failWithErrors deve criar um resultado de falha com múltiplas mensagens")
    void resultTFailWithErrorsTest() {
        List<String> errorMessages = Arrays.asList("Erro 1", "Erro 2");
        ResultT<String> result = ResultT.failWithErrors(errorMessages);
        
        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertEquals(2, result.getErrors().size());
        assertEquals("Erro 1", result.getErrors().get(0));
        assertEquals("Erro 2", result.getErrors().get(1));
        assertNull(result.getData());
    }
    
    @Test
    @DisplayName("ValidationResult.ok() deve criar um ValidationResult de sucesso")
    void validationResultOkTest() {
        ValidationResult result = ValidationResult.ok();
        
        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertTrue(result.getErrors().isEmpty());
        assertTrue(result.getValidationErrors().isEmpty());
    }
    
    @Test
    @DisplayName("ValidationResult.addError deve adicionar um erro a um campo específico")
    void validationResultAddErrorTest() {
        ValidationResult result = ValidationResult.ok();
        result.addError("campo1", "Valor inválido");
        
        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertEquals(1, result.getErrors().size());
        assertEquals("campo1: Valor inválido", result.getErrors().get(0));
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().containsKey("campo1"));
        assertEquals(1, result.getValidationErrors().get("campo1").size());
        assertEquals("Valor inválido", result.getValidationErrors().get("campo1").get(0));
    }
}
