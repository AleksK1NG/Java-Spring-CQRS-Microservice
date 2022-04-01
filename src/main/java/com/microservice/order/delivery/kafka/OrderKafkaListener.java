package com.microservice.order.delivery.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.configuration.OrderKafkaTopics;
import com.microservice.order.domain.Order;
import com.microservice.shared.serializer.JsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaListener {
    private final ObjectMapper objectMapper;
    private final JsonSerializer jsonSerializer;
    private final OrderKafkaTopics orderKafkaTopics;

    @KafkaListener(topics = {"${order.kafka.topics.order-address-changed}"}, groupId = "order_microservice", concurrency = "10")
    public void changeDeliveryAddressListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack, @Header("Alex") byte[] header) {
        log.info("(Listener) topic: {}, partition: {}, timestamp: {}, offset: {}, data: {}", meta.topic(), meta.partition(), meta.timestamp(), meta.offset(), new String(data));

        log.info("headers: {}", new String(header));
        try {
            Order order = jsonSerializer.deserializeFromJsonBytes(data, Order.class);
            ack.acknowledge();
            log.info("ack order: {}", order);
        } catch (Exception e) {
            ack.nack(1000);
            log.error("jsonSerializer.deserializeFromJsonBytes: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = {"${order.kafka.topics.order-status-updated}"}, groupId = "order_microservice", concurrency = "10")
    public void updateOrderStatusListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(updateOrderStatusListener) data: {}", new String(data));

        try {
            final var order = objectMapper.readValue(data, Order.class);
            ack.acknowledge();
            log.info("ack order: {}", order);
        } catch (IOException e) {
            ack.nack(1000);
            log.error("objectMapper.readValue: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = {"${order.kafka.topics.order-created}"}, groupId = "order_microservice", concurrency = "10")
    public void createOrderListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(createOrderListener) data: {}", new String(data));

        try {
            final var order = objectMapper.readValue(data, Order.class);
            ack.acknowledge();
            log.info("ack order: {}", order);
        } catch (IOException e) {
            ack.nack(1000);
            log.error("objectMapper.readValue: {}", e.getMessage());
        }

    }
}
