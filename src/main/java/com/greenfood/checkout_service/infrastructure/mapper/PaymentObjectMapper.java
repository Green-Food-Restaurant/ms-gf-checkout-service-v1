package com.greenfood.checkout_service.infrastructure.mapper;

import org.mapstruct.Mapper;
import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.dtos.ProductDto;
import com.greenfood.checkout_service.domain.model.Payment;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.CheckoutEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ProductEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.out.repository.entity.CheckoutEntity;

@Mapper(componentModel = "spring")
public interface PaymentObjectMapper {
    PaymentDto checkoutEventToPaymentDto(CheckoutEventDto checkoutEvent);

    ProductDto productEventToProductDto(ProductEventDto productEvent);

    CheckoutEntity paymentDtoToCheckoutEntity(PaymentDto paymentDto);

    Payment paymentDtoToPayment(PaymentDto paymentDto);

    PaymentDto paymentToPaymentDto(Payment payment);
}
