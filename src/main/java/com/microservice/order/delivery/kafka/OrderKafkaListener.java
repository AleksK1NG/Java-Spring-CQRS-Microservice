package com.microservice.order.delivery.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.events.EventsHandler;
import com.microservice.order.events.OrderCreatedEvent;
import com.microservice.order.events.OrderDeliveryAddressChangedEvent;
import com.microservice.order.events.OrderStatusUpdatedEvent;
import com.microservice.shared.serializer.JsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaListener {
    private final ObjectMapper objectMapper;
    private final JsonSerializer jsonSerializer;
    private final EventsHandler eventsHandler;
    private final Tracer tracer;

    @KafkaListener(topics = {"${order.kafka.topics.order-address-changed}"}, groupId = "${order.kafka.groupId}", concurrency = "10")
    @NewSpan(name = "(changeDeliveryAddressListener)")
    public void changeDeliveryAddressListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("data", new String(data)));
        logEvent(data, meta);

        try {
            final var event = jsonSerializer.deserializeFromJsonBytes(data, OrderDeliveryAddressChangedEvent.class);
            ack.acknowledge();
            log.info("ack event: {}", event);
        } catch (Exception e) {
            ack.nack(1000);
            log.error("changeDeliveryAddressListener: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = {"${order.kafka.topics.order-status-updated}"}, groupId = "${order.kafka.groupId}", concurrency = "10")
    @NewSpan(name = "(updateOrderStatusListener)")
    public void updateOrderStatusListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        logEvent(data, meta);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("data", new String(data)));

        try {
            final var event = objectMapper.readValue(data, OrderStatusUpdatedEvent.class);
            eventsHandler.handle(event);
            ack.acknowledge();
            log.info("ack event: {}", event);
        } catch (IOException e) {
            ack.nack(1000);
            log.error("updateOrderStatusListener: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = {"${order.kafka.topics.order-created}"}, groupId = "${order.kafka.groupId}", concurrency = "10")
    @NewSpan(name = "(createOrderListener)")
    public void createOrderListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        logEvent(data, meta);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("data", new String(data)));

        try {
            final var event = objectMapper.readValue(data, OrderCreatedEvent.class);
            eventsHandler.handle(event);
            ack.acknowledge();
            log.info("ack event: {}", event);
        } catch (IOException e) {
            ack.nack(1000);
            log.error("createOrderListener: {}", e.getMessage());
        }
    }

    private void logEvent(byte[] data, ConsumerRecordMetadata meta) {
        log.info("topic: {}, partition: {}, offset: {}, timestamp: {}, data: {},", meta.topic(), meta.partition(), meta.offset(), meta.timestamp(), new String(data));
    }
}
