package com.food_delivery_service.delivery_service.kafka.listeners;

import com.food_delivery_service.delivery_service.entity.DeliveryEntity;
import com.food_delivery_service.delivery_service.repository.DeliveryJpaRepository;
import com.food_delivery_system.kafka.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class OrderPaidKafkaConsumer {

    private final DeliveryJpaRepository deliveryJpaRepository;

    @Autowired
    public OrderPaidKafkaConsumer(DeliveryJpaRepository deliveryJpaRepository) {
        this.deliveryJpaRepository = deliveryJpaRepository;
    }

    @KafkaListener(topics = "orders.events", groupId = "order-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void orderPaidEventConsumerListener(ConsumerRecord<String, OrderPaidEvent> consumerRecord) {
        log.info("Received OrderPaidEvent from Kafka topic: {}", consumerRecord.topic());
        log.info("Received OrderPaidEvent from Kafka topic: {}", consumerRecord.value());

        Long orderId = consumerRecord.value().orderId();

        Optional<DeliveryEntity> theDelivery = deliveryJpaRepository.findByOrderId(orderId);
        if(theDelivery.isPresent()) {
            log.info("Found order delivery has been already assigned: delivery={}", theDelivery.get());
            return;
        }

        DeliveryEntity newDelivery = DeliveryEntity.builder()
                .orderId(consumerRecord.value().orderId())
                .courierName("courier-" + ThreadLocalRandom.current().nextInt(30))
                .etaMinutes(ThreadLocalRandom.current().nextInt(10, 45))
                .build();
        deliveryJpaRepository.save(newDelivery);
        log.info("Delivery for orderId={} has been successfully assigned, and will be finished in {} minutes", newDelivery.getOrderId(), newDelivery.getEtaMinutes());
    }
}
