package com.greenfood.checkout_service.infrastructure.adapter.out.repository;

import org.springframework.stereotype.Component;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.port.out.PaymentRepositoryPortOut;
import com.greenfood.checkout_service.infrastructure.adapter.out.repository.client.MongoClientRepository;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentRepositoryAdapterOut implements PaymentRepositoryPortOut {

    private MongoClientRepository mongoClientRepository;
    private PaymentObjectMapper paymentObjectMapper;

    public PaymentRepositoryAdapterOut(MongoClientRepository mongoClientRepository, PaymentObjectMapper paymentObjectMapper) {
        this.mongoClientRepository = mongoClientRepository;
        this.paymentObjectMapper = paymentObjectMapper;
    }

    @Override
    public void save(PaymentDto paymentDto) {
        this.mongoClientRepository.save(paymentObjectMapper.paymentDtoToCheckoutEntity(paymentDto));
    }

    @Override
    public PaymentDto findById(String paymentId) {
        log.info("Buscando pagamento com ID: {}", paymentId);
        return paymentObjectMapper.checkoutEntityToPaymentDto(
            this.mongoClientRepository.findByCartId(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + paymentId))
        );
    }

}
