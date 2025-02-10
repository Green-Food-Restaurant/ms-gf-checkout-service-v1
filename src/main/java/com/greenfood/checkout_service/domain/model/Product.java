package com.greenfood.checkout_service.domain.model;

import java.math.BigDecimal;

public class Product {
    private String id; // Identificador único do produto
    private String name; // Nome do produto
    private String description; // Descrição do produto
    private BigDecimal price; // Preço do produto
    private String category; // Categoria do produto

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    //Gere os getters e setters
    
}
