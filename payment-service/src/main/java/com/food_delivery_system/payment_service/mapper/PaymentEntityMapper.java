package com.food_delivery_system.payment_service.mapper;

import com.food_delivery_system.http.payment.CreatePaymentResponseDTO;
import com.food_delivery_system.payment_service.entity.payment.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PaymentEntityMapper {

    @Mapping(source = "id", target = "paymentId")
    CreatePaymentResponseDTO toCreatePaymentResponse(PaymentEntity paymentEntity);
}
