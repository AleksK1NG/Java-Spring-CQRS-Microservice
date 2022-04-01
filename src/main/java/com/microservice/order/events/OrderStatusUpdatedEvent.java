package com.microservice.order.events;

import com.microservice.order.domain.OrderStatus;

public record OrderStatusUpdatedEvent(OrderStatus status) {
}
