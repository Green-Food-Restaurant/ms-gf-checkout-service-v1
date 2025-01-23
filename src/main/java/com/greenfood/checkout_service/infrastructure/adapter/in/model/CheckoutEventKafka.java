package com.greenfood.checkout_service.infrastructure.adapter.in.model;

import lombok.Data;

@Data
public class CheckoutEventKafka {
    private String userId;
    private Number totalAmount;
    private Number productQuantity;
}
