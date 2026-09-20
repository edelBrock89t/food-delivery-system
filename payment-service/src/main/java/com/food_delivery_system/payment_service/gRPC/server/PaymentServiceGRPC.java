package com.food_delivery_system.payment_service.gRPC.server;

import com.food_delivery_system.grpc.order_payment.CreatePaymentRequest;
import com.food_delivery_system.grpc.order_payment.CreatePaymentResponse;
import com.food_delivery_system.grpc.order_payment.PaymentMethod;
import com.food_delivery_system.grpc.order_payment.PaymentServiceGrpc;
import com.food_delivery_system.http.payment.PaymentStatus;
import com.food_delivery_system.payment_service.converter.CreatePaymentRequesDTOConverter;
import com.food_delivery_system.payment_service.entity.payment.PaymentEntity;
import com.food_delivery_system.payment_service.gRPC.errors.PaymentFailedException;
import com.food_delivery_system.payment_service.service.PaymentService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class PaymentServiceGRPC extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final PaymentService paymentService;
    private final CreatePaymentRequesDTOConverter createPaymentRequesDTOConverter;

    @Autowired
    public PaymentServiceGRPC(PaymentService paymentService, CreatePaymentRequesDTOConverter createPaymentRequesDTOConverter) {
        this.paymentService = paymentService;
        this.createPaymentRequesDTOConverter = createPaymentRequesDTOConverter;
    }

    @Override
    public void processPayment(CreatePaymentRequest request, StreamObserver<CreatePaymentResponse> responseObserver) {

        PaymentEntity paymentEntity = paymentService.createPayment(createPaymentRequesDTOConverter.convert(request));

        if(paymentEntity.getPaymentStatus() == PaymentStatus.PAYMENT_SUCCEEDED) {
             CreatePaymentResponse paymentResponse= CreatePaymentResponse.newBuilder()
                     .setPaymentId(paymentEntity.getId())
                     .setPaymentStatus(com.food_delivery_system.grpc.order_payment.PaymentStatus.valueOf(paymentEntity.getPaymentStatus().name()))
                     .setOrderId(paymentEntity.getOrderId())
                     .setPaymentMethod(PaymentMethod.valueOf(paymentEntity.getPaymentMethod().name()))
                     .setAmount(paymentEntity.getAmount().longValue())
                     .build();

            responseObserver.onNext(paymentResponse);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(
                    Status.FAILED_PRECONDITION
                            .withDescription("Payment for orderId=" + paymentEntity.getOrderId() + " failed")
                            .asRuntimeException()
            );
        }
    }
}
