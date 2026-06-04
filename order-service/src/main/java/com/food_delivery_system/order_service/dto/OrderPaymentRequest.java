package com.food_delivery_system.order_service.dto;

import com.food_delivery_system.http.payment.PaymentMethod;

public record OrderPaymentRequest(
        PaymentMethod paymentMethod
) {
}
