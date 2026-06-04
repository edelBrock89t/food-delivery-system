package com.food_delivery_system.order_service.external;

import com.food_delivery_system.http.payment.CreatePaymentRequest;
import com.food_delivery_system.http.payment.CreatePaymentResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
        accept = "application/json",
        contentType = "application/json",
        url = "/api/payments"
)
public interface PaymentHttpClient {

    @PostExchange
    CreatePaymentResponse createPayment(@RequestBody CreatePaymentRequest createPaymentRequest);

}
