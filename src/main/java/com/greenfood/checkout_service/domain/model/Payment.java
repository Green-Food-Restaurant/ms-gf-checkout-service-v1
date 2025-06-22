package com.greenfood.checkout_service.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

import com.greenfood.checkout_service.domain.enums.PaymentStatus;

public class Payment {
    private String id;
    private String cartId;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private Number prodcutQuantity;
    private Number paymentAmount;
    private List<Product> products;

    public Number getProdcutQuantity() {
        return prodcutQuantity;
    }

    public void setProdcutQuantity(Number prodcutQuantity) {
        this.prodcutQuantity = prodcutQuantity;
    }

    public void setPaymentAmount(Number paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Number getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Payment [id=" + id + ", cartId=" + cartId + ", paymentStatus=" + paymentStatus + ", paymentDate="
                + paymentDate + ", paymentAmount=" + paymentAmount + ", products=" + products + "]";
    }
}
