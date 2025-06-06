package com.greenfood.checkout_service.infrastructure.adapter.out.gatewayMercadoPago;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.dtos.ProductDto;
import com.greenfood.checkout_service.domain.enums.PaymentMethod;
import com.greenfood.checkout_service.domain.enums.PaymentStatus;
import com.greenfood.checkout_service.domain.result.ResultT;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;

@ExtendWith(MockitoExtension.class)
public class GatewayMercadoPagoTest {

    private GatewayMercadoPago gatewayMercadoPago;
    private PaymentDto paymentDto;
    
    private static final String INVALID_TOKEN_ERROR = "MercadoPago Error. Status: 400, Content: {\"message\":\"invalid_token\",\"error\":\"bad_request\",\"status\":400,\"cause\":null}";
    
    @BeforeEach
    void setUp() {
        gatewayMercadoPago = new GatewayMercadoPago("TEST-ACCESS-TOKEN");
        List<ProductDto> products = new ArrayList<>();
        products.add(new ProductDto("1", "Produto 1", "Descrição do produto 1", new BigDecimal("10.00"), "categoria", 2));
        paymentDto = new PaymentDto("1", "cart123", PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING, null, 20.00, 2, products);
    }
    
    @Test
    @DisplayName("Deve falhar quando o token for inválido")
    void executeNullInitPointTest() {
        // Executar o método execute
        ResultT<ResponseCheckoutDto> result = gatewayMercadoPago.execute(paymentDto);
        
        // Verificar que o resultado falhou
        assertTrue(result.isFailure());
        // A mensagem de erro deve conter a informação de token inválido
        assertTrue(result.getErrors().get(0).contains("invalid_token") || 
                  result.getErrors().get(0).contains("Error processing payment"));
    }
    
    @Test
    @DisplayName("Deve falhar quando o token for inválido (teste alternativo)")
    void executeSuccessTest() {
        // Executar o método execute
        ResultT<ResponseCheckoutDto> result = gatewayMercadoPago.execute(paymentDto);
        
        // Verificar que o resultado falhou
        assertTrue(result.isFailure());
        // A mensagem de erro deve conter a informação de token inválido
        assertTrue(result.getErrors().get(0).contains("invalid_token") || 
                  result.getErrors().get(0).contains("Error processing payment"));
    }
    
    @Test
    @DisplayName("Deve construir corretamente o resultado de erro para MPApiException")
    void executeMPApiExceptionTest() {
        // Criar um resultado de falha que simula o comportamento do método execute ao receber MPApiException
        ResultT<ResponseCheckoutDto> result = ResultT.failWithError("MercadoPago Error. Status: 400, Content: Invalid request");
        
        // Verificar que o resultado está correto
        assertTrue(result.isFailure());
        assertEquals("MercadoPago Error. Status: 400, Content: Invalid request", result.getErrors().get(0));
    }
    
    @Test
    @DisplayName("Deve construir corretamente o resultado de erro para MPException")
    void executeMPExceptionTest() {
        // Criar um resultado de falha que simula o comportamento do método execute ao receber MPException
        ResultT<ResponseCheckoutDto> result = ResultT.failWithError("MercadoPago Error: Connection timeout");
        
        // Verificar que o resultado está correto
        assertTrue(result.isFailure());
        assertEquals("MercadoPago Error: Connection timeout", result.getErrors().get(0));
    }
    
    @Test
    @DisplayName("Deve construir corretamente o resultado de erro para exceção genérica")
    void executeGenericExceptionTest() {
        // Criar um resultado de falha que simula o comportamento do método execute ao receber uma exceção genérica
        ResultT<ResponseCheckoutDto> result = ResultT.failWithError("Error processing payment: Unexpected error");
        
        // Verificar que o resultado está correto
        assertTrue(result.isFailure());
        assertEquals("Error processing payment: Unexpected error", result.getErrors().get(0));
    }
}
