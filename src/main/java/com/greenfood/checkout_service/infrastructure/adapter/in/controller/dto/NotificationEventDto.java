package com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto;

public record NotificationEventDto(
    String action,
    String api_version,
    Data data,
    String date_created,
    String id,
    boolean live_mode,
    String type,
    Long user_id
) {
    public record Data(String id) {}
}
