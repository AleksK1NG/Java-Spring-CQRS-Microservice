package com.microservice.order.mappers;

import com.microservice.order.commands.CreateOrderCommand;
import com.microservice.order.domain.Order;
import com.microservice.order.dto.OrderResponseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

public final class OrderMapper {

    public static OrderResponseDto orderResponseDtoFromEntity(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId().toString())
                .userEmail(order.getUserEmail())
                .userName(order.getUserName())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryDate(order.getDeliveryDate())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .status(order.getStatus().name())
                .build();
    }

    public static Order orderFromCreateOrderCommand(CreateOrderCommand command) {
        return Order.builder()
                .id(UUID.fromString(command.getId()))
                .userEmail(command.getUserEmail())
                .userName(command.getUserName())
                .status(command.getStatus())
                .deliveryAddress(command.getDeliveryAddress())
                .deliveryDate(command.getDeliveryDate())
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
    }
}
