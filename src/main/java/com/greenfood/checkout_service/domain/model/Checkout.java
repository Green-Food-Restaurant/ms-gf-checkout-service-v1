package com.greenfood.checkout_service.domain.model;

public class Checkout {
    private String id;
    private String cartId;
    private String statusCheckout;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentDate;
    private String paymentAmount;

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

    public String getStatusCheckout() {
        return statusCheckout;
    }

    public void setStatusCheckout(String statusCheckout) {
        this.statusCheckout = statusCheckout;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public String toString() {
        return "Checkout [id=" + id + ", cartId=" + cartId + ", statusCheckout=" + statusCheckout + ", paymentMethod="
                + paymentMethod + ", paymentStatus=" + paymentStatus + ", paymentDate=" + paymentDate
                + ", paymentAmount=" + paymentAmount + "]";
    }
}
