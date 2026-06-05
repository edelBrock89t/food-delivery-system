package com.food_delivery_system.order_service.kafka.consumer;

import com.food_delivery_system.kafka.DeliveryAssignedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeliveryAssignedConsumer {

    @KafkaListener(topics = "delivery.events", groupId = "delivery-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void deliveryAssignedKafkaListener(ConsumerRecord<String, DeliveryAssignedEvent> consumerRecord) {
        log.info("Received delivery assigned event: {}", consumerRecord.value());
    }
}
