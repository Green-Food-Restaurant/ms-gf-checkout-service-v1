package com.greenfood.checkout_service.application.useCases;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebhookNotificationUseCase {
    public void handle(String payload) {
        // Aqui você pode fazer o parse do payload e tratar os eventos do Mercado Pago
        log.info("Processando evento do webhook Mercado Pago: {}", payload);
        // TODO: Adicionar lógica para tratar payment.updated, chargebacks, fraudes, etc
    }
}
