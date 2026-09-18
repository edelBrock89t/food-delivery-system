package com.food_delivery_system.order_service.config;

import com.food_delivery_system.grpc.order_payment.PaymentServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcChannelsConfig {

    @Bean
    public PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub(GrpcChannelFactory channelFactory) {
        return PaymentServiceGrpc.newBlockingStub(channelFactory.createChannel("payment-service-client"));
    }
}
