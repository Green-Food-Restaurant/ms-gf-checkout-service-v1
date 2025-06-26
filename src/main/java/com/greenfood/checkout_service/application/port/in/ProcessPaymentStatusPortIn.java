package com.greenfood.checkout_service.application.port.in;

import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.NotificationEventDto;

public interface ProcessPaymentStatusPortIn {
    
    /**
     * Processa o status do pagamento.
     *
     * @param paymentId o ID do pagamento a ser processado
     * @return um objeto ResultT contendo o status do pagamento
     */
    void execute(NotificationEventDto notificationEventDto);
}
