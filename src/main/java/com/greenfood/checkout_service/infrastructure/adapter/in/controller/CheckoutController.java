package com.greenfood.checkout_service.infrastructure.adapter.in.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.port.in.PaymentOrderPortIn;
import com.greenfood.checkout_service.application.port.in.ProcessPaymentStatusPortIn;
import com.greenfood.checkout_service.domain.result.ResultT;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.CheckoutEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.NotificationEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;
import com.greenfood.checkout_service.infrastructure.exceptions.response.ApiErrorResponse;
import com.greenfood.checkout_service.infrastructure.exceptions.response.ApiSuccessResponse;
import com.greenfood.checkout_service.infrastructure.mapper.PaymentObjectMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/v1/checkout")
public class CheckoutController {
    private final PaymentObjectMapper paymentObjectMapper;
    private final PaymentOrderPortIn paymentOrderPortIn;
    private final ProcessPaymentStatusPortIn processPaymentStatusPortIn;

    public CheckoutController(PaymentObjectMapper paymentObjectMapper, PaymentOrderPortIn paymentOrderPortIn, ProcessPaymentStatusPortIn processPaymentStatusPortIn) {
        this.paymentObjectMapper = paymentObjectMapper;
        this.paymentOrderPortIn = paymentOrderPortIn;
        this.processPaymentStatusPortIn = processPaymentStatusPortIn;
    }

    @PostMapping(
        value = "/create",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@Valid @RequestBody CheckoutEventDto checkoutEvent) {
        log.info("Iniciando processamento do checkout");
        log.info("Dados do checkout recebidos: {}", checkoutEvent);
        
        log.info("Convertendo CheckoutEventDto para PaymentDto");
        PaymentDto paymentDto = paymentObjectMapper.checkoutEventToPaymentDto(checkoutEvent);
        log.info("Conversão realizada com sucesso. PaymentDto: {}", paymentDto);
        
        log.info("Executando processamento do pagamento");
        ResultT<ResponseCheckoutDto> result = paymentOrderPortIn.execute(paymentDto);
        
        if (result.isSuccess()) {
            log.info("Processamento do checkout finalizado com sucesso");
            return ResponseEntity.ok(
                new ApiSuccessResponse<>(
                    200, 
                    "Checkout processado com sucesso", 
                    result.getData()
                )
            );
        } else {
            log.error("Falha no processamento do checkout: {}", result.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(
                    400, 
                    "Falha no processamento do checkout", 
                    result.getErrors()
                ));
        }
    }

    @PostMapping(
        value = "/callback/notification",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> callbackNotification(@Valid @RequestBody NotificationEventDto notificationEvent) {
        log.info("Recebendo notificação do Mercado Pago");
        log.info("Dados da notificação recebidos: {}", notificationEvent);

        processPaymentStatusPortIn.execute(notificationEvent);

        return ResponseEntity.ok().build();
    }
}

