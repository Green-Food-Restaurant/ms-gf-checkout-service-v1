package com.greenfood.checkout_service.infrastructure.adapter.in.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.extern.slf4j.Slf4j;

import com.greenfood.checkout_service.application.useCases.WebhookNotificationUseCase;

@Slf4j
@RestController
@RequestMapping("/v1/webhook")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WebhookNotificationController {

    private final WebhookNotificationUseCase webhookNotificationUseCase;

    public WebhookNotificationController(WebhookNotificationUseCase webhookNotificationUseCase) {
        this.webhookNotificationUseCase = webhookNotificationUseCase;
    }

    @PostMapping("/mercadopago")
    public ResponseEntity<String> receiveMercadoPagoWebhook(
            @RequestBody(required = false) String payload,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId,
            @RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor) {
        webhookNotificationUseCase.handle(payload);
        return ResponseEntity.status(HttpStatus.OK).body("Webhook recebido com sucesso");
    }
}
