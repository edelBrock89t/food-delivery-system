package com.food_delivery_system.order_service.errors;

public class PaymentServiceUnavailableException extends Exception {

    public PaymentServiceUnavailableException(String message) {
        super(message);
    }
}
