package com.food_delivery_system.payment_service.gRPC.errors;

public class PaymentFailedException extends Exception {

    public PaymentFailedException(String message) {
        super(message);
    }
}
