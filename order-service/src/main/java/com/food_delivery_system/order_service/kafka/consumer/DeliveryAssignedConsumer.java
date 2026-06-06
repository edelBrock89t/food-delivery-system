package com.food_delivery_system.order_service.kafka.consumer;

import com.food_delivery_system.kafka.DeliveryAssignedEvent;
import com.food_delivery_system.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeliveryAssignedConsumer {

    private final OrderService orderService;

    @Autowired
    public DeliveryAssignedConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${delivery.kafka.topic.delivery-assigned}", groupId = "delivery-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void deliveryAssignedKafkaListener(ConsumerRecord<String, DeliveryAssignedEvent> consumerRecord) {
        log.info("Received delivery assigned event: {}", consumerRecord.value());

        Long orderId = Long.valueOf(consumerRecord.key());

        orderService.assignDeliveryToOrder(orderId, consumerRecord.value());
        log.info("Updated info for Order with orderId={}", orderId);
    }
}
