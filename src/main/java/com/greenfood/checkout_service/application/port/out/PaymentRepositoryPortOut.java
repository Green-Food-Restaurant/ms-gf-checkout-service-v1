package com.greenfood.checkout_service.application.port.out;

import com.greenfood.checkout_service.application.dtos.PaymentDto;

public interface PaymentRepositoryPortOut {

    void save(PaymentDto paymentDto);

}
