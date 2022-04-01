package com.microservice.order.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.configuration.OrderKafkaTopics;
import com.microservice.order.domain.OrderStatus;
import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.order.mappers.OrderMapper;
import com.microservice.order.repository.OrderPostgresRepository;
import com.microservice.shared.serializer.JsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderCommandsHandler implements CommandsHandler {
    private final OrderPostgresRepository postgresRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final OrderKafkaTopics orderKafkaTopics;
    private final JsonSerializer jsonSerializer;

    @Override
    public String handle(CreateOrderCommand command) {
        final var order = OrderMapper.orderFromCreateOrderCommand(command);
        final var savedOrder = postgresRepository.save(order);
        publishMessage(orderKafkaTopics.getOrderCreatedTopic(), savedOrder, null);
        log.info("savedOrder: {}", savedOrder);
        return savedOrder.getId().toString();
    }

    @Override
    @Transactional
    public void handle(UpdateOrderStatusCommand command) {
        final var orderOptional = postgresRepository.findById(UUID.fromString(command.getId()));
        if (orderOptional.isEmpty()) throw new OrderNotFoundException("order not found: " + command.getId());

        final var order = orderOptional.get();
        order.setStatus(OrderStatus.valueOf(command.getStatus().toString()));
        order.setUpdatedAt(ZonedDateTime.now());
        postgresRepository.save(order);
        publishMessage(orderKafkaTopics.getOrderStatusUpdatedTopic(), order, Map.of("Alex", "PRO".getBytes(StandardCharsets.UTF_8)));
    }



    @Override
    @Transactional
    public void handle(ChangeDeliveryAddressCommand command) {
        final var orderOptional = postgresRepository.findById(UUID.fromString(command.getId()));
        if (orderOptional.isEmpty()) throw new OrderNotFoundException("order not found: " + command.getId());

        final var order = orderOptional.get();
        order.setDeliveryAddress(command.getDeliveryAddress());
        order.setUpdatedAt(ZonedDateTime.now());
        postgresRepository.save(order);
        publishMessage(orderKafkaTopics.getOrderAddressChangedTopic(), order, null);
    }

    private void publishMessage(String topic, Object data, Map<String, byte[]> headers) {
        try {
            byte[] bytes = jsonSerializer.serializeToJsonBytes(data);
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, bytes);

            if (headers != null) {
                headers.forEach((key, value) -> record.headers().add(key, value));
            }

            kafkaTemplate.send(record).get(1000, TimeUnit.MILLISECONDS);
            log.info("send success: {}", record);
        } catch (Exception e) {
            log.error("publishMessage error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
