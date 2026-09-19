package com.food_delivery_system.order_service.gRPC.client;

import com.food_delivery_system.grpc.order_payment.CreatePaymentRequest;
import com.food_delivery_system.grpc.order_payment.CreatePaymentResponse;
import com.food_delivery_system.grpc.order_payment.PaymentServiceGrpc;
import com.food_delivery_system.order_service.errors.OrderNotFoundException;
import com.food_delivery_system.order_service.errors.PaymentFailedException;
import com.food_delivery_system.order_service.errors.PaymentServiceUnavailableException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceClient {

    private PaymentServiceGrpc.PaymentServiceBlockingStub stub;

    @Autowired
    public PaymentServiceClient(PaymentServiceGrpc.PaymentServiceBlockingStub stub) {
        this.stub = stub;
    }

    public CreatePaymentResponse createPayment(CreatePaymentRequest request) throws PaymentFailedException, OrderNotFoundException, PaymentServiceUnavailableException {

        try {
            CreatePaymentResponse response = stub.processPayment(request);

            return CreatePaymentResponse.newBuilder()
                    .setPaymentId(response.getPaymentId())
                    .setPaymentStatus(response.getPaymentStatus())
                    .setOrderId(response.getOrderId())
                    .setPaymentMethod(response.getPaymentMethod())
                    .setAmount(response.getAmount())
                    .build();
        }catch (StatusRuntimeException e) {
            Status status = e.getStatus();
            switch (status.getCode()) {
                case FAILED_PRECONDITION -> throw new PaymentFailedException(status.getDescription());
                case NOT_FOUND -> throw new OrderNotFoundException(status.getDescription());
                case UNAVAILABLE -> throw new PaymentServiceUnavailableException(status.getDescription());
                default -> throw new PaymentFailedException(status.getDescription());
            }
        }

    }
}
