package com.microservice.order.queries;

import com.microservice.order.domain.OrderStatus;

public record GetOrdersByStatusQuery(OrderStatus status, int page, int size) {
}
