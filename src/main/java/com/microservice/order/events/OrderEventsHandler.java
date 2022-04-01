package com.microservice.order.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.OrderDocument;
import com.microservice.order.repository.OrderMongoRepository;
import com.microservice.shared.serializer.JsonSerializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class OrderEventsHandler implements EventsHandler {
    private final OrderMongoRepository orderMongoRepository;
    private final ObjectMapper objectMapper;
    private final JsonSerializer jsonSerializer;

    @Override
    public void handle(OrderCreatedEvent event) {

        final var document = OrderDocument.builder()
                .id(event.id())
                .userEmail(event.userEmail())
                .userName(event.userName())
                .deliveryAddress(event.deliveryAddress())
                .deliveryDate(event.deliveryDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(event.status())
                .build();
        final var insert = orderMongoRepository.insert(document);
        log.info("insert: {}", insert);
    }

    @Override
    public void handle(OrderStatusUpdatedEvent event) {
        final var document = orderMongoRepository.findById(event.id());
        if (document.isEmpty()) throw new RuntimeException("order not found exception");

        document.get().setStatus(event.status());
        orderMongoRepository.save(document.get());
    }

    @Override
    public void handle(OrderDeliveryAddressChangedEvent event) {
        final var document = orderMongoRepository.findById(event.id());
        if (document.isEmpty()) throw new RuntimeException("order not found exception");

        document.get().setDeliveryAddress(event.deliveryAddress());
        orderMongoRepository.save(document.get());
    }
}
