package com.food_delivery_system.order_service.errors;

public class PaymentFailedException extends Exception {

    public PaymentFailedException(String message) {
        super(message);
    }
}
