package com.food_delivery_system.payment_service.converter;

import com.food_delivery_system.grpc.order_payment.CreatePaymentRequest;
import com.food_delivery_system.http.payment.CreatePaymentRequestDTO;
import com.food_delivery_system.http.payment.PaymentMethod;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreatePaymentRequesDTOConverter implements Converter<CreatePaymentRequest, CreatePaymentRequestDTO> {

    @Override
    public CreatePaymentRequestDTO convert(CreatePaymentRequest source) {
        return new CreatePaymentRequestDTO(
                source.getOrderId(),
                PaymentMethod.valueOf(source.getPaymentMethod().name()),
                BigDecimal.valueOf(source.getAmount())
        );
    }
}
