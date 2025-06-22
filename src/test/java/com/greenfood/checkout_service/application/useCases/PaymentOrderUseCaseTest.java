package com.greenfood.checkout_service.application.useCases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.dtos.ProductDto;
import com.greenfood.checkout_service.application.port.out.GatewayMercadoPagoPortOut;
import com.greenfood.checkout_service.application.port.out.PaymentRepositoryPortOut;
import com.greenfood.checkout_service.domain.enums.PaymentStatus;
import com.greenfood.checkout_service.domain.model.Payment;
import com.greenfood.checkout_service.domain.result.ResultT;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

@ExtendWith(MockitoExtension.class)
public class PaymentOrderUseCaseTest {

    @Mock
    private PaymentRepositoryPortOut paymentRepositoryPortOut;
    
    @Mock
    private PaymentObjectMapper paymentObjectMapper;
    
    @Mock
    private GatewayMercadoPagoPortOut gatewayMercadoPagoPortOut;
    
    private PaymentOrderUseCase paymentOrderUseCase;
    
    private PaymentDto paymentDto;
    private Payment payment;
    
    @BeforeEach
    void setUp() {
        paymentOrderUseCase = new PaymentOrderUseCase(paymentRepositoryPortOut, paymentObjectMapper, gatewayMercadoPagoPortOut);
        List<ProductDto> products = new ArrayList<>();
        products.add(new ProductDto("1", "Produto 1", "Descrição do produto 1", new BigDecimal("10.00"), "categoria", 2));
        paymentDto = new PaymentDto("1", "cart123", PaymentStatus.PENDING, null, 20.00, 2, products);
        payment = new Payment();
        payment.setId("1");
        payment.setCartId("cart123");
    }
    
    @Test
    @DisplayName("Deve retornar sucesso quando o gateway retornar sucesso")
    void executeSuccessTest() {
        // Arrange
        ResponseCheckoutDto responseDto = new ResponseCheckoutDto("http://payment-url.com");
        ResultT<ResponseCheckoutDto> gatewayResult = ResultT.ok(responseDto);
        
        when(paymentObjectMapper.paymentDtoToPayment(paymentDto)).thenReturn(payment);
        when(gatewayMercadoPagoPortOut.execute(paymentDto)).thenReturn(gatewayResult);
        
        // Act
        ResultT<ResponseCheckoutDto> result = paymentOrderUseCase.execute(paymentDto);
        
        // Assert
        assertTrue(result.isSuccess());
        assertEquals(responseDto, result.getData());
        assertEquals(PaymentStatus.COMPLETED, payment.getPaymentStatus());
        
        verify(paymentRepositoryPortOut).save(any());
    }
    
    @Test
    @DisplayName("Deve retornar falha quando o gateway retornar falha")
    void executeGatewayFailureTest() {
        // Arrange
        ResultT<ResponseCheckoutDto> gatewayResult = ResultT.failWithError("Erro no gateway de pagamento");
        
        when(paymentObjectMapper.paymentDtoToPayment(paymentDto)).thenReturn(payment);
        when(gatewayMercadoPagoPortOut.execute(paymentDto)).thenReturn(gatewayResult);
        
        // Act
        ResultT<ResponseCheckoutDto> result = paymentOrderUseCase.execute(paymentDto);
        
        // Assert
        assertTrue(result.isFailure());
        assertEquals(PaymentStatus.ERROR, payment.getPaymentStatus());
        assertEquals("Erro no gateway de pagamento", result.getErrors().get(0));
        
        verify(paymentRepositoryPortOut).save(any());
    }
    
    @Test
    @DisplayName("Deve retornar falha quando ocorrer exceção")
    void executeExceptionTest() {
        // Arrange
        when(paymentObjectMapper.paymentDtoToPayment(paymentDto)).thenReturn(payment);
        when(gatewayMercadoPagoPortOut.execute(paymentDto)).thenThrow(new RuntimeException("Erro inesperado"));
        
        // Act
        ResultT<ResponseCheckoutDto> result = paymentOrderUseCase.execute(paymentDto);
        
        // Assert
        assertTrue(result.isFailure());
        assertEquals(PaymentStatus.ERROR, payment.getPaymentStatus());
        assertTrue(result.getErrors().get(0).contains("Erro ao processar pagamento"));
        
        verify(paymentRepositoryPortOut).save(any());
    }
}
