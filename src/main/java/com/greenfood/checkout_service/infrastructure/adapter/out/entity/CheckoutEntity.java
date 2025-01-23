package com.greenfood.checkout_service.infrastructure.adapter.out.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document(collection = "checkout")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CheckoutEntity {
    @Id
    private String id;
    private String cartId;
    private String statusCheckout;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentDate;
    private String paymentAmount;
}
