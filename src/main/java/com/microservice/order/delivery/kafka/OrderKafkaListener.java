package com.microservice.order.delivery.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaListener {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"change_delivery_address"}, groupId = "order_microservice", concurrency = "10")
    public void changeDeliveryAddressListener(@Payload byte[] data, Acknowledgment ack)  {
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
