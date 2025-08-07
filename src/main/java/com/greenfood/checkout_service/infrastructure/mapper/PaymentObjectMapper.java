package com.greenfood.checkout_service.infrastructure.mapper;

import org.mapstruct.Mapper;
import com.greenfood.checkout_service.application.dtos.PaymentDto;
import com.greenfood.checkout_service.application.dtos.ProductDto;
import com.greenfood.checkout_service.domain.model.Payment;
import com.greenfood.checkout_service.domain.model.Product;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.CheckoutEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto.ProductEventDto;
import com.greenfood.checkout_service.infrastructure.adapter.out.repository.entity.CheckoutEntity;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentObjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(source = "totalAmount", target = "paymentAmount")
    PaymentDto checkoutEventToPaymentDto(CheckoutEventDto checkoutEvent);

    ProductDto productEventToProductDto(ProductEventDto productEvent);

    CheckoutEntity paymentDtoToCheckoutEntity(PaymentDto paymentDto);

    Payment paymentDtoToPayment(PaymentDto paymentDto);

    PaymentDto paymentToPaymentDto(Payment payment);

    ProductDto productDtoToProduct(Product product);

    // CheckoutEntity não tem productQuantity e products, então definir valores padrão
    @Mapping(target = "productQuantity", expression = "java(0)")
    @Mapping(target = "products", expression = "java(java.util.Collections.emptyList())")
    PaymentDto checkoutEntityToPaymentDto(CheckoutEntity checkoutEntity);
}
