package com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto;

import java.util.List;

import com.greenfood.checkout_service.domain.enums.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record CheckoutEventDto(
    @NotNull(message = "cartId não pode ser nulo")
    @NotEmpty(message = "cartId não pode estar vazio") 
    String cartId,

    @NotNull(message = "totalAmount não pode ser nulo")
    Number totalAmount,

    @NotNull(message = "productQuantity não pode ser nulo") 
    Number productQuantity,

    @NotNull(message = "paymentMethod não pode ser nulo")
    PaymentMethod paymentMethod,

    @NotNull(message = "products não pode ser nulo")
    @NotEmpty(message = "products não pode estar vazio")
    @Valid
    List<ProductEventDto> products
) {}
