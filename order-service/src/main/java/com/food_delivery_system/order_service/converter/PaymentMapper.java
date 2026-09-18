package com.food_delivery_system.order_service.converter;

import com.food_delivery_system.grpc.order_payment.CreatePaymentRequest;
import com.food_delivery_system.grpc.order_payment.CreatePaymentResponse;
import com.food_delivery_system.http.payment.CreatePaymentRequestDTO;
import com.food_delivery_system.http.payment.CreatePaymentResponseDTO;
import com.food_delivery_system.http.payment.PaymentMethod;
import com.food_delivery_system.http.payment.PaymentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
    PaymentStatus toDomain(com.food_delivery_system.grpc.order_payment.PaymentStatus proto);

    @ValueMappings({
            @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED"),
            @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "PAYMENT_METHOD_UNSPECIFIED")
    })
    PaymentMethod toDomain(com.food_delivery_system.grpc.order_payment.PaymentMethod proto);


    CreatePaymentResponseDTO toDomain(CreatePaymentResponse proto);
    CreatePaymentResponse toProto(CreatePaymentResponseDTO domain);

    CreatePaymentRequestDTO toDomain(CreatePaymentRequest proto);
    CreatePaymentRequest toProto(CreatePaymentRequestDTO domain);
}
