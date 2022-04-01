package com.microservice.order.delivery.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.events.OrderCreatedEvent;
import com.microservice.order.events.OrderDeliveryAddressChangedEvent;
import com.microservice.order.events.OrderStatusUpdatedEvent;
import com.microservice.shared.serializer.JsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaListener {
    private final ObjectMapper objectMapper;
    private final JsonSerializer jsonSerializer;

    @KafkaListener(topics = {"${order.kafka.topics.order-address-changed}"}, groupId = "order_microservice", concurrency = "10")
    public void changeDeliveryAddressListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(Listener) topic: {}, partition: {}, timestamp: {}, offset: {}, data: {}", meta.topic(), meta.partition(), meta.timestamp(), meta.offset(), new String(data));

        try {
            final var event = jsonSerializer.deserializeFromJsonBytes(data, OrderDeliveryAddressChangedEvent.class);
            ack.acknowledge();
            log.info("ack event: {}", event);
        } catch (Exception e) {
            ack.nack(1000);
            log.error("changeDeliveryAddressListener: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = {"${order.kafka.topics.order-status-updated}"}, groupId = "order_microservice", concurrency = "10")
    public void updateOrderStatusListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(updateOrderStatusListener) data: {}", new String(data));

        try {
            final var event = objectMapper.readValue(data, OrderStatusUpdatedEvent.class);
            ack.acknowledge();
            log.info("ack event: {}", event);
        } catch (IOException e) {
            ack.nack(1000);
            log.error("updateOrderStatusListener: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = {"${order.kafka.topics.order-created}"}, groupId = "order_microservice", concurrency = "10")
    public void createOrderListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(createOrderListener) data: {}", new String(data));

        try {
            final var event = objectMapper.readValue(data, OrderCreatedEvent.class);
            ack.acknowledge();
            log.info("ack event: {}", event);
        } catch (IOException e) {
            ack.nack(1000);
            log.error("createOrderListener: {}", e.getMessage());
        }

    }
}
