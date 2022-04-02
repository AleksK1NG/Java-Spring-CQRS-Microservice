package com.microservice.order.events;

import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.order.mappers.OrderMapper;
import com.microservice.order.repository.OrderMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class OrderEventsHandler implements EventsHandler {
    private final OrderMongoRepository orderMongoRepository;
    private final Tracer tracer;

    @Override
    @NewSpan(name = "(OrderCreatedEvent)")
    public void handle(OrderCreatedEvent event) {
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("event", event.toString()));

        final var document = OrderMapper.orderDocumentFromCreateEvent(event);
        final var insert = orderMongoRepository.insert(document);
        log.info("created mongodb order: {}", insert);

        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("insert", insert.toString()));
    }

    @Override
    @NewSpan(name = "(OrderStatusUpdatedEvent)")
    public void handle(OrderStatusUpdatedEvent event) {
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("event", event.toString()));

        final var document = orderMongoRepository.findById(event.id());
        if (document.isEmpty()) throw new OrderNotFoundException("order not found exception");

        document.get().setStatus(event.status());
        orderMongoRepository.save(document.get());

        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("document", document.toString()));
    }

    @Override
    @NewSpan(name = "(OrderDeliveryAddressChangedEvent)")
    public void handle(OrderDeliveryAddressChangedEvent event) {
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("event", event.toString()));

        final var document = orderMongoRepository.findById(event.id());
        if (document.isEmpty()) throw new OrderNotFoundException("order not found exception");

        document.get().setDeliveryAddress(event.deliveryAddress());
        orderMongoRepository.save(document.get());

        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("document", document.toString()));
    }
}
