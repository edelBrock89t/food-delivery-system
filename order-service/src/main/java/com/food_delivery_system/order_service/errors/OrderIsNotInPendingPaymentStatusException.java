package com.food_delivery_system.order_service.errors;

public class OrderIsNotInPendingPaymentStatusException extends Exception {
    public OrderIsNotInPendingPaymentStatusException(String message) {
        super(message);
    }
}
