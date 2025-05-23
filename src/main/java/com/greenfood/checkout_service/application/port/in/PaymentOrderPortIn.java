package com.greenfood.checkout_service.application.port.in;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.domain.result.ResultT;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;

public interface PaymentOrderPortIn {
    
    ResultT<ResponseCheckoutDto> execute(PaymentDto paymentDto);
}
