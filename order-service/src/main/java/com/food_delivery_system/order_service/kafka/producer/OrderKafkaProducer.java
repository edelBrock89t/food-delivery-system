package com.food_delivery_system.order_service.kafka.producer;

import com.food_delivery_system.http.order.OrderStatus;
import com.food_delivery_system.http.payment.CreatePaymentResponse;
import com.food_delivery_system.kafka.OrderPaidEvent;
import com.food_delivery_system.order_service.entity.order.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OrderKafkaProducer {

    @Value("${order.kafka.topics}")
    private String ordersTopic;
    private final KafkaTemplate<String, OrderPaidEvent> kafkaTemplate;

    @Autowired
    public OrderKafkaProducer(KafkaTemplate<String, OrderPaidEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderPaidEvent(OrderEntity orderEntity, CreatePaymentResponse paymentResponse) {
        if(orderEntity.getOrderStatus().equals(OrderStatus.PAYMENT_FAILED)) {
            log.error("Cannot assign delivery for order with orderId={}, because it's not paid", orderEntity.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot assign delivery for order with orderId={" + orderEntity.getId() + "}, because it's not paid");
        }

        OrderPaidEvent orderPaidEvent = new OrderPaidEvent(
                orderEntity.getId(),
                paymentResponse.paymentId(),
                orderEntity.getTotalAmount(),
                paymentResponse.paymentMethod(),
                LocalDateTime.now()
        );
        kafkaTemplate.send(ordersTopic, orderEntity.getId().toString(), orderPaidEvent)
                .thenAccept(result -> {
                    log.info("Payment for orderId id={} has been proceeded with status orderStatus={}", orderEntity.getId(), orderEntity.getOrderStatus());
                });
    }
}
