package com.greenfood.checkout_service.application.port.out;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;

public interface GatewayMercadoPagoPortOut {
    ResponseCheckoutDto execute(PaymentDto paymentDto);
}
