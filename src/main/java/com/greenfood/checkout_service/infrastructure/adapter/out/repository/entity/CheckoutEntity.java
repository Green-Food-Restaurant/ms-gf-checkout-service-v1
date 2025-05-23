package com.greenfood.checkout_service.infrastructure.adapter.out.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.greenfood.checkout_service.domain.enums.PaymentMethod;
import com.greenfood.checkout_service.domain.enums.PaymentStatus;

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
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String paymentDate;
    private Number paymentAmount;
}
