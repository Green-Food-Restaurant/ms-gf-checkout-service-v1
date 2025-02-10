package com.greenfood.checkout_service.infrastructure.adapter.in.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductEventDto(
    @NotNull(message = "id não pode ser nulo")
    @NotEmpty(message = "id não pode estar vazio")
    String id, // Identificador único do produto


    @NotNull(message = "name não pode ser nulo")
    @NotEmpty(message = "name não pode estar vazio")
    String name, // Nome do produto

    @NotNull(message = "description não pode ser nulo")
    @NotEmpty(message = "description não pode estar vazio")
    String description, // Descrição do produto

    @NotNull(message = "price não pode ser nulo")
    BigDecimal price, // Preço do produto

    // @NotNull(message = "category não pode ser nulo")
    // @NotEmpty(message = "category não pode estar vazio")
    String category, // Categoria do produto

    @NotNull(message = "quantity não pode ser nulo")
    Integer quantity // Quantidade do produto
) {}
