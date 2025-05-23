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
    PaymentDto checkoutEventToPaymentDto(CheckoutEventDto checkoutEvent);

    ProductDto productEventToProductDto(ProductEventDto productEvent);

    // @Mapping(target = "productQuantity", ignore = true)
    // @Mapping(target = "products", ignore = true)
    CheckoutEntity paymentDtoToCheckoutEntity(PaymentDto paymentDto);

    Payment paymentDtoToPayment(PaymentDto paymentDto);

    PaymentDto paymentToPaymentDto(Payment payment);

    ProductDto productDtoToProduct(Product productDto);
}
