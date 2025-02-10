package com.greenfood.checkout_service.infrastructure.adapter.in.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.port.in.PaymentOrderPortIn;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.CheckoutEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/v1/checkout")
public class CheckoutController {
    private final PaymentObjectMapper paymentObjectMapper;
    private final PaymentOrderPortIn paymentOrderPortIn;
    
    public CheckoutController(PaymentObjectMapper paymentObjectMapper, PaymentOrderPortIn paymentOrderPortIn) {
        this.paymentObjectMapper = paymentObjectMapper;
        this.paymentOrderPortIn = paymentOrderPortIn;
    }

    @PostMapping(
        value = "/create",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseCheckoutDto> create(@Valid @RequestBody CheckoutEventDto checkoutEvent) {
        log.info("Iniciando processamento do checkout");
        log.info("Dados do checkout recebidos: {}", checkoutEvent);
        

        log.info("Convertendo CheckoutEventDto para PaymentDto");
        PaymentDto paymentDto = paymentObjectMapper.checkoutEventToPaymentDto(checkoutEvent);
        log.info("Conversão realizada com sucesso. PaymentDto: {}", paymentDto);
        
        log.info("Executando processamento do pagamento");
        ResponseCheckoutDto responseCheckoutDto = paymentOrderPortIn.execute(paymentDto);
        log.info("Processamento do checkout finalizado com sucesso");
        return ResponseEntity.ok(responseCheckoutDto);

    }





}

