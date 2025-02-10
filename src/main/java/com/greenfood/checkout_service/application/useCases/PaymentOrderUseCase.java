package com.greenfood.checkout_service.application.useCases;

import java.time.LocalDateTime;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.port.in.PaymentOrderPortIn;
import com.greenfood.checkout_service.application.port.out.GatewayMercadoPagoPortOut;
import com.greenfood.checkout_service.application.port.out.PaymentRepositoryPortOut;
import com.greenfood.checkout_service.domain.enums.PaymentStatus;
import com.greenfood.checkout_service.domain.model.Payment;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;
import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.PaymentProcessingException;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentOrderUseCase implements PaymentOrderPortIn {

    private PaymentRepositoryPortOut paymentRepositoryPortOut;
    private PaymentObjectMapper paymentObjectMapper;
    private GatewayMercadoPagoPortOut gatewayMercadoPagoPortOut;

    public PaymentOrderUseCase(PaymentRepositoryPortOut paymentRepositoryPortOut, PaymentObjectMapper paymentObjectMapper, GatewayMercadoPagoPortOut gatewayMercadoPagoPortOut) {
        this.paymentRepositoryPortOut = paymentRepositoryPortOut;
        this.paymentObjectMapper = paymentObjectMapper;
        this.gatewayMercadoPagoPortOut = gatewayMercadoPagoPortOut;
    }       

    @Override
    public ResponseCheckoutDto execute(PaymentDto paymentDto) {
        ResponseCheckoutDto responseCheckoutDto = null;

        log.info("Execute Payment order use case");
        Payment payment = paymentObjectMapper.paymentDtoToPayment(paymentDto);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        log.info("Start payment process");
        
        try {
            responseCheckoutDto = gatewayMercadoPagoPortOut.execute(paymentDto);
        } finally {
            if (responseCheckoutDto == null) {
                payment.setPaymentStatus(PaymentStatus.ERROR);
            }
            paymentRepositoryPortOut.save(paymentObjectMapper.paymentToPaymentDto(payment));
            log.info("Payment order use case finished");
        }

        return responseCheckoutDto;
    }


}
