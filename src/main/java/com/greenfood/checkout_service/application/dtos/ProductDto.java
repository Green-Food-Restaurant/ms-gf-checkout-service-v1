package com.greenfood.checkout_service.application.dtos;

import java.math.BigDecimal;

public record ProductDto(
    String id, // Identificador único do produto
    String name, // Nome do produto
    String description, // Descrição do produto
    BigDecimal price, // Preço do produto
    String category, // Categoria do produto
    Integer quantity // Quantidade do produto
) {
    
}
