package com.microservice.order.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.configuration.OrderKafkaTopicsConfiguration;
import com.microservice.mappers.OrderMapper;
import com.microservice.order.domain.OrderStatus;
import com.microservice.order.events.OrderDeliveryAddressChangedEvent;
import com.microservice.order.events.OrderStatusUpdatedEvent;
import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.order.repository.OrderPostgresRepository;
import com.microservice.shared.serializer.JsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderCommandsHandler implements CommandsHandler {
    private final OrderPostgresRepository postgresRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final OrderKafkaTopicsConfiguration orderKafkaTopicsConfiguration;
    private final JsonSerializer jsonSerializer;
    private final Tracer tracer;

    @Override
    @NewSpan(name = "(CreateOrderCommand)")
    public String handle(CreateOrderCommand command) {
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("CreateOrderCommand", command.toString()));

        final var order = OrderMapper.orderFromCreateOrderCommand(command);
        final var savedOrder = postgresRepository.save(order);
        final var event = OrderMapper.orderCreatedEventFromOrder(order);

        publishMessage(orderKafkaTopicsConfiguration.getOrderCreatedTopic(), event, null);
        log.info("savedOrder: {}", savedOrder);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("savedOrder", savedOrder.toString()));
        return savedOrder.getId().toString();
    }

    @Override
    @Transactional
    @NewSpan(name = "(UpdateOrderStatusCommand)")
    public void handle(UpdateOrderStatusCommand command) {
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("UpdateOrderStatusCommand", command.toString()));

        final var orderOptional = postgresRepository.findById(UUID.fromString(command.getId()));
        if (orderOptional.isEmpty()) throw new OrderNotFoundException("order not found: " + command.getId());

        final var order = orderOptional.get();
        order.setStatus(OrderStatus.valueOf(command.getStatus().toString()));
        order.setUpdatedAt(LocalDateTime.now());

        postgresRepository.save(order);

        final var event = new OrderStatusUpdatedEvent(order.getId().toString(), order.getStatus());
        publishMessage(orderKafkaTopicsConfiguration.getOrderStatusUpdatedTopic(), event, Map.of("traceId", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));

        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("event", event.toString()));
    }


    @Override
    @Transactional
    @NewSpan(name = "(ChangeDeliveryAddressCommand)")
    public void handle(ChangeDeliveryAddressCommand command) {
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("ChangeDeliveryAddressCommand", command.toString()));

        final var orderOptional = postgresRepository.findById(UUID.fromString(command.getId()));
        if (orderOptional.isEmpty()) throw new OrderNotFoundException("order not found: " + command.getId());

        final var order = orderOptional.get();
        order.setDeliveryAddress(command.getDeliveryAddress());
        order.setUpdatedAt(LocalDateTime.now());

        postgresRepository.save(order);

        final var event = new OrderDeliveryAddressChangedEvent(order.getId().toString(), order.getDeliveryAddress());
        publishMessage(orderKafkaTopicsConfiguration.getOrderAddressChangedTopic(), event, null);

        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("event", event.toString()));
    }

    @NewSpan(name = "(publishMessage)")
    private void publishMessage(String topic, Object data, Map<String, byte[]> headers) {

        try {
            byte[] bytes = jsonSerializer.serializeToJsonBytes(data);
            Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("data", new String(bytes)));
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, bytes);

            if (headers != null) {
                headers.forEach((key, value) -> record.headers().add(key, value));
            }

            kafkaTemplate.send(record).get(1000, TimeUnit.MILLISECONDS);
            log.info("send success: {}", record);
            Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("record", record.toString()));
        } catch (Exception e) {
            log.error("publishMessage error: {}", e.getMessage());
            Optional.ofNullable(tracer.currentSpan()).map(span -> span.error(e));
            throw new RuntimeException(e);
        }
    }
}
