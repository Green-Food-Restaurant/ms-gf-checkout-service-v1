package com.greenfood.checkout_service.infrastructure.adapter.in.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.port.in.PaymentOrderPortIn;
import com.greenfood.checkout_service.domain.enums.PaymentMethod;
import com.greenfood.checkout_service.domain.result.ResultT;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.CheckoutEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ProductEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;
import com.greenfood.checkout_service.infrastructure.exceptions.response.ApiSuccessResponse;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

@ExtendWith(MockitoExtension.class)
public class CheckoutControllerTest {

    @Mock
    private PaymentObjectMapper paymentObjectMapper;
    
    @Mock
    private PaymentOrderPortIn paymentOrderPortIn;
    
    @InjectMocks
    private CheckoutController checkoutController;
    
    private CheckoutEventDto checkoutEventDto;
    private PaymentDto paymentDto;
    
    @BeforeEach
    void setUp() {
        List<ProductEventDto> products = new ArrayList<>();
        products.add(new ProductEventDto("1", "Produto 1", "Descrição do produto 1", new BigDecimal("10.00"), "categoria", 2));
        
        checkoutEventDto = new CheckoutEventDto(
            "cart123", 
            20.00, 
            2, 
            PaymentMethod.CREDIT_CARD, 
            products
        );
        
        paymentDto = mock(PaymentDto.class);
    }
    
    @Test
    @DisplayName("Deve retornar resposta de sucesso quando o caso de uso retornar sucesso")
    void createSuccessTest() {
        // Arrange
        ResponseCheckoutDto responseDto = new ResponseCheckoutDto("http://payment-url.com");
        ResultT<ResponseCheckoutDto> result = ResultT.ok(responseDto);
        
        when(paymentObjectMapper.checkoutEventToPaymentDto(checkoutEventDto)).thenReturn(paymentDto);
        when(paymentOrderPortIn.execute(paymentDto)).thenReturn(result);
        
        // Act
        ResponseEntity<?> response = checkoutController.create(checkoutEventDto);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        ApiSuccessResponse<?> successResponse = (ApiSuccessResponse<?>) response.getBody();
        assertNotNull(successResponse);
        assertEquals(200, successResponse.getStatusCode());
        assertEquals("Checkout processado com sucesso", successResponse.getMessage());
        assertEquals(responseDto, successResponse.getData());
    }
    
    @Test
    @DisplayName("Deve retornar resposta de erro quando o caso de uso retornar falha")
    void createFailureTest() {
        // Arrange
        ResultT<ResponseCheckoutDto> result = ResultT.failWithError("Erro no processamento do pagamento");
        
        when(paymentObjectMapper.checkoutEventToPaymentDto(checkoutEventDto)).thenReturn(paymentDto);
        when(paymentOrderPortIn.execute(paymentDto)).thenReturn(result);
        
        // Act
        ResponseEntity<?> response = checkoutController.create(checkoutEventDto);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
