package com.greenfood.checkout_service.application.dtos;

import java.util.List;

import com.greenfood.checkout_service.domain.enums.PaymentStatus;

public record PaymentDto(
    String id,
    String cartId,
    PaymentStatus paymentStatus,
    String paymentDate,
    Number paymentAmount,
    Number productQuantity,
    List<ProductDto> products
) {
    
}
