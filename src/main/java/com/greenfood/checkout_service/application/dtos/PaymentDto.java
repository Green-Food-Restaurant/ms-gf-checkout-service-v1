package com.greenfood.checkout_service.application.dtos;

import java.util.List;

import com.greenfood.checkout_service.domain.enums.PaymentMethod;
import com.greenfood.checkout_service.domain.enums.PaymentStatus;

public record PaymentDto(
    String id,
    String cartId,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    String paymentDate,
    Number totalAmount,
    Number productQuantity,
    List<ProductDto> products
) {
    
}
