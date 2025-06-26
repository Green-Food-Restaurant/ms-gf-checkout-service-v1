package com.greenfood.checkout_service.infrastructure.configuration.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.greenfood.checkout_service.application.port.in.ProcessPaymentStatusPortIn;
import com.greenfood.checkout_service.application.port.out.PaymentRepositoryPortOut;
import com.greenfood.checkout_service.application.useCases.ProcessPaymentStatusUseCase;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

@Configuration
public class ProcessPaymentStatusConfigBean {

    @Bean
    public ProcessPaymentStatusPortIn processPaymentStatusPortIn(PaymentRepositoryPortOut paymentRepositoryPortOut, 
                                                                PaymentObjectMapper paymentObjectMapper) {
        return new ProcessPaymentStatusUseCase(paymentRepositoryPortOut, paymentObjectMapper);
    }

}
