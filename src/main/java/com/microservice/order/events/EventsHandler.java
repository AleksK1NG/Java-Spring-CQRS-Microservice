package com.microservice.order.events;

public interface EventsHandler {
    void handle(OrderCreatedEvent event);
    void handle(OrderStatusUpdatedEvent event);
    void handle(OrderDeliveryAddressChangedEvent event);
}
