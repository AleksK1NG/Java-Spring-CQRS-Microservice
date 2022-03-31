package com.microservice.order.delivery.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.Order;
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

    @KafkaListener(topics = {"change_delivery_address"}, groupId = "order_microservice", concurrency = "10")
    public void changeDeliveryAddressListener(
            @Payload byte[] data,
            ConsumerRecordMetadata meta,
            Acknowledgment ack) {
        log.info("(Listener) topic: {}, partition: {}, timestamp: {}, offset: {}, data: {}", meta.topic(), meta.partition(), meta.timestamp(), meta.offset(), new String(data));

        try {
            Order order = jsonSerializer.deserializeFromJsonBytes(data, Order.class);
            ack.acknowledge();
            log.info("ack order: {}", order);
        } catch (Exception e) {
            ack.nack(1000);
            log.error("jsonSerializer.deserializeFromJsonBytes: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = {"update_order_status"}, groupId = "order_microservice", concurrency = "10")
    public void updateOrderStatusListener(@Payload byte[] data, Acknowledgment ack) {
        log.info("(Listener) data: {}", new String(data));

        try {
            final var order = objectMapper.readValue(data, Order.class);
            ack.acknowledge();
            log.info("ack order: {}", order);
        } catch (IOException e) {
            ack.nack(1000);
            log.error("objectMapper.readValue: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = {"create_order"}, groupId = "order_microservice", concurrency = "10")
    public void createOrderListener(@Payload byte[] data, Acknowledgment ack) {
        log.info("(Listener) data: {}", new String(data));

        try {
            final var order = objectMapper.readValue(data, Order.class);
            ack.acknowledge();
            log.info("ack order: {}", order);
        } catch (IOException e) {
            ack.nack(1000);
            log.error("objectMapper.readValue: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
