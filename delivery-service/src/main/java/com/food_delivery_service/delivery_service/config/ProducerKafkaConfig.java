package com.food_delivery_service.delivery_service.config;

import com.food_delivery_system.kafka.DeliveryAssignedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerKafkaConfig {

    @Bean
    public ProducerFactory<String, DeliveryAssignedEvent> producerFactory(JsonMapper jsonMapper) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        JacksonJsonSerializer<DeliveryAssignedEvent> serializer = new JacksonJsonSerializer<>(jsonMapper);
        serializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(properties, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate(ProducerFactory<String, DeliveryAssignedEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
