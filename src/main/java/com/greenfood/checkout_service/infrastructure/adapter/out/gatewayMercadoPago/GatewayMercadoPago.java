package com.greenfood.checkout_service.infrastructure.adapter.out.gatewayMercadoPago;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.dtos.ProductDto;
import com.greenfood.checkout_service.application.port.out.GatewayMercadoPagoPortOut;
import com.greenfood.checkout_service.domain.result.ResultT;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ResponseCheckoutDto;
import com.greenfood.checkout_service.infrastructure.adapter.out.gatewayMercadoPago.utils.GsonBuilderMPConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GatewayMercadoPago implements GatewayMercadoPagoPortOut {

    private String accessToken;

    public GatewayMercadoPago(@Value("${gateway.mercadopago.access-token}") String accessToken) {
        this.accessToken = accessToken;
    }

    public ResultT<ResponseCheckoutDto> execute(PaymentDto paymentDto) {
        PreferenceClient preferenceClient = new PreferenceClient();


        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .accessToken(accessToken)
                .connectionRequestTimeout(2000)
                .connectionTimeout(2000)
                .socketTimeout(2000)
                .build();

        PreferenceRequest preferenceRequest = createPreferenceRequest(createPreferenceItemRequests(paymentDto.products()));

        try {
            log.info("Init Mercado Pago API");
            Preference preference = preferenceClient.create(preferenceRequest, requestOptions);
            log.info("Preference: {}", preference.getInitPoint());
            if (preference.getInitPoint() == null) {
                return ResultT.failWithError("Preference not created");
            }
            log.info("Preference created: {}", preference.getInitPoint());
            log.info("Response Mercado Pago API: " + GsonBuilderMPConfig.gson().toJson(preference));

            return ResultT.ok(new ResponseCheckoutDto(preference.getInitPoint()));
        } catch (MPApiException ex) {
            String errorMsg = "MercadoPago Error. Status: " + ex.getApiResponse().getStatusCode() + ", Content: " + ex.getApiResponse().getContent();
            log.error(errorMsg);
            return ResultT.failWithError(errorMsg);
        } catch (MPException ex) {
            String errorMsg = "MercadoPago Error: " + ex.getMessage();
            log.error(errorMsg);
            return ResultT.failWithError(errorMsg);
        } catch (Exception ex) {
            String errorMsg = "Error processing payment: " + ex.getMessage();
            log.error(errorMsg);
            return ResultT.failWithError(errorMsg);
        }
    }

    private PreferenceRequest createPreferenceRequest(List<PreferenceItemRequest> preferenceItemRequests) {
        return PreferenceRequest.builder()
                .items(preferenceItemRequests)
                .build();
    }

    private List<PreferenceItemRequest> createPreferenceItemRequests(List<ProductDto> products) {
        return products.stream()
                .map(this::mapperPaymentDtoToPreferenceItemRequest)
                .collect(Collectors.toList());
    }

    private PreferenceItemRequest mapperPaymentDtoToPreferenceItemRequest(ProductDto productDto) {
        return PreferenceItemRequest.builder()
                .id(productDto.id())
                .title(productDto.name())
                .quantity(productDto.quantity())
                .unitPrice(productDto.price())
                .description(productDto.description())
                .currencyId("BRL")
                .build();
    }
}
