package com.greenfood.checkout_service.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

import com.greenfood.checkout_service.domain.enums.PaymentMethod;
import com.greenfood.checkout_service.domain.enums.PaymentStatus;

public class Payment {
    private String id;
    private String cartId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private BigDecimal paymentAmount;
    private List<Product> products;

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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public BigDecimal getPaymentAmount() {
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
        return "Payment [id=" + id + ", cartId=" + cartId + ", paymentMethod="
                + paymentMethod + ", paymentStatus=" + paymentStatus + ", paymentDate=" + paymentDate
                + ", paymentAmount=" + paymentAmount + ", products=" + products + "]";
    }
}
