package com.food_delivery_service.delivery_service.kafka.listeners;

import com.food_delivery_system.kafka.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderPaidKafkaConsumer {

    @KafkaListener(topics = "orders.events", groupId = "order-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void orderPaidEventConsumerListener(ConsumerRecord<String, OrderPaidEvent> consumerRecord) {
        log.info("Received OrderPaidEvent from Kafka topic: {}", consumerRecord.topic());
        log.info("Received OrderPaidEvent from Kafka topic: {}", consumerRecord.value());
    }
}
