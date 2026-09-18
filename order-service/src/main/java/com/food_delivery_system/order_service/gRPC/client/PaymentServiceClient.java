package com.food_delivery_system.order_service.gRPC.client;

import com.food_delivery_system.grpc.order_payment.CreatePaymentRequest;
import com.food_delivery_system.grpc.order_payment.CreatePaymentResponse;
import com.food_delivery_system.grpc.order_payment.PaymentServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceClient {

    private PaymentServiceGrpc.PaymentServiceBlockingStub stub;

    @Autowired
    public PaymentServiceClient(PaymentServiceGrpc.PaymentServiceBlockingStub stub) {
        this.stub = stub;
    }

    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {

        CreatePaymentResponse response = stub.processPayment(request);

        return CreatePaymentResponse.newBuilder()
                .setPaymentId(response.getPaymentId())
                .setPaymentStatus(response.getPaymentStatus())
                .setOrderId(response.getOrderId())
                .setPaymentMethod(response.getPaymentMethod())
                .setAmount(response.getAmount())
                .build();
    }
}
