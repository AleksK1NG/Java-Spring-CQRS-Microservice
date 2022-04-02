package com.microservice.order.events;

import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.order.mappers.OrderMapper;
import com.microservice.order.repository.OrderMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderEventsHandler implements EventsHandler {
    private final OrderMongoRepository orderMongoRepository;

    @Override
    public void handle(OrderCreatedEvent event) {
        final var document = OrderMapper.orderDocumentFromCreateEvent(event);
        final var insert = orderMongoRepository.insert(document);
        log.info("created mongodb order: {}", insert);
    }

    @Override
    public void handle(OrderStatusUpdatedEvent event) {
        final var document = orderMongoRepository.findById(event.id());
        if (document.isEmpty()) throw new OrderNotFoundException("order not found exception");

        document.get().setStatus(event.status());
        orderMongoRepository.save(document.get());
    }

    @Override
    public void handle(OrderDeliveryAddressChangedEvent event) {
        final var document = orderMongoRepository.findById(event.id());
        if (document.isEmpty()) throw new OrderNotFoundException("order not found exception");

        document.get().setDeliveryAddress(event.deliveryAddress());
        orderMongoRepository.save(document.get());
    }
}
