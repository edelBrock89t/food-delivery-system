package com.food_delivery_system.order_service.kafka.producer;

import com.food_delivery_system.kafka.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderKafkaProducer {

    private final KafkaTemplate<String, OrderPaidEvent> kafkaTemplate;

    @Autowired
    public OrderKafkaProducer(KafkaTemplate<String, OrderPaidEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderPaidEvent(OrderPaidEvent orderPaidEvent) {
        kafkaTemplate.send("orders", orderPaidEvent.orderId().toString(), orderPaidEvent);
        log.info("Order Paid Event has been sent to Kafka: {}", orderPaidEvent);
    }
}
