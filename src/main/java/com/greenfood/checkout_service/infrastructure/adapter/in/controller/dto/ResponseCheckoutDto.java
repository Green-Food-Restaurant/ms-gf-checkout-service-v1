package com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto;

public record ResponseCheckoutDto(
    String initPoint
) {
    public ResponseCheckoutDto(String initPoint) {
        this.initPoint = initPoint;
    }
}
