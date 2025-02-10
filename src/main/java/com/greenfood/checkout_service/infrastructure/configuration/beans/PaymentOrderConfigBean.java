package com.greenfood.checkout_service.infrastructure.configuration.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.greenfood.checkout_service.application.port.out.GatewayMercadoPagoPortOut;
import com.greenfood.checkout_service.application.port.out.PaymentRepositoryPortOut;
import com.greenfood.checkout_service.application.useCases.PaymentOrderUseCase;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

@Configuration
public class PaymentOrderConfigBean {

    @Bean
    public PaymentOrderUseCase paymentOrderUseCase(PaymentRepositoryPortOut paymentRepositoryPortOut, PaymentObjectMapper paymentObjectMapper, GatewayMercadoPagoPortOut gatewayMercadoPagoPortOut) {
        return new PaymentOrderUseCase(paymentRepositoryPortOut, paymentObjectMapper, gatewayMercadoPagoPortOut);
    }
}
