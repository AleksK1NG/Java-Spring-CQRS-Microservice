package com.microservice.order.events;

import com.microservice.order.domain.OrderStatus;

import java.time.ZonedDateTime;

public record OrderCreatedEvent(
        String userEmail,
        String userName,
        String deliveryAddress,
        OrderStatus status,
        ZonedDateTime deliveryDate) {
}
