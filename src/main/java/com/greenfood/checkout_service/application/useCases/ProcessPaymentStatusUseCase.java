package com.greenfood.checkout_service.application.useCases;

import com.greenfood.checkout_service.application.port.in.ProcessPaymentStatusPortIn;
import com.greenfood.checkout_service.application.port.out.PaymentRepositoryPortOut;
import com.greenfood.checkout_service.domain.model.Payment;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.NotificationEventDto;
import com.greenfood.checkout_service.infrastructure.exceptions.templateExceptions.PaymentProcessingException;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

public class ProcessPaymentStatusUseCase implements ProcessPaymentStatusPortIn {

    private final PaymentRepositoryPortOut paymentRepositoryPortOut;
    private final PaymentObjectMapper paymentObjectMapper;

    public ProcessPaymentStatusUseCase(PaymentRepositoryPortOut paymentRepositoryPortOut, PaymentObjectMapper paymentObjectMapper) {
        this.paymentRepositoryPortOut = paymentRepositoryPortOut;
        this.paymentObjectMapper = paymentObjectMapper;
    }

    @Override
    public void execute(NotificationEventDto notificationEventDto) {
        try {
            String paymentId = notificationEventDto.data().id();
            Payment payment = paymentObjectMapper.paymentDtoToPayment(paymentRepositoryPortOut.findById(paymentId));

            if (payment == null) {
                throw new PaymentProcessingException("Pagamento não encontrado");
            }

            // Aqui você pode processar o status do pagamento conforme necessário
            // Por exemplo, atualizar o status do pagamento com base no evento recebido

            System.out.println("Processando status do pagamento: " + paymentId);
        } catch (Exception e) {
            throw new PaymentProcessingException("Erro ao processar status do pagamento: " + e.getMessage());
        }
    }



}