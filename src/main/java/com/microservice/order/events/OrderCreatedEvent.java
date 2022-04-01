package com.microservice.order.events;

import com.microservice.order.domain.OrderStatus;

import java.time.LocalDateTime;

public record OrderCreatedEvent(
        String id,
        String userEmail,
        String userName,
        String deliveryAddress,
        OrderStatus status,
        LocalDateTime deliveryDate) {
}
