package com.microservice.configuration;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class OrderKafkaTopicsConfiguration {

    @Value(value = "${order.kafka.topics.order-created:order-created}")
    private String orderCreatedTopic;

    @Value(value = "${order.kafka.topics.order-address-changed:order-address-changed}")
    private String orderAddressChangedTopic;

    @Value(value = "${order.kafka.topics.order-status-updated:order-status-updated}")
    private String orderStatusUpdatedTopic;
}
