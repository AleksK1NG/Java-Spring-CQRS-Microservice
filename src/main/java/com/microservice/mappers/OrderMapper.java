package com.microservice.mappers;

import com.microservice.order.commands.CreateOrderCommand;
import com.microservice.order.domain.Order;
import com.microservice.order.domain.OrderDocument;
import com.microservice.order.domain.OrderStatus;
import com.microservice.order.dto.CreateOrderDTO;
import com.microservice.order.dto.OrderResponseDto;
import com.microservice.order.events.OrderCreatedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderResponseDto orderResponseDtoFromEntity(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId().toString())
                .userEmail(order.getUserEmail())
                .userName(order.getUserName())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryDate(order.getDeliveryDate().toString())
                .createdAt(order.getCreatedAt().toString())
                .updatedAt(order.getUpdatedAt().toString())
                .status(order.getStatus().name())
                .build();
    }

    public static OrderResponseDto orderResponseDtoFromDocument(OrderDocument order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .userEmail(order.getUserEmail())
                .userName(order.getUserName())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryDate(order.getDeliveryDate().toString())
                .createdAt(order.getCreatedAt().toString())
                .updatedAt(order.getUpdatedAt().toString())
                .status(order.getStatus().name())
                .build();
    }

    public static OrderDocument orderDocumentFromEntity(Order order) {
        return OrderDocument.builder()
                .id(order.getId().toString())
                .userEmail(order.getUserEmail())
                .userName(order.getUserName())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryDate(order.getDeliveryDate())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .status(order.getStatus())
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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static OrderDocument orderDocumentFromCreateEvent(OrderCreatedEvent event) {
        return OrderDocument.builder()
                .id(event.id())
                .userEmail(event.userEmail())
                .userName(event.userName())
                .deliveryAddress(event.deliveryAddress())
                .deliveryDate(event.deliveryDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(event.status())
                .build();
    }

    public static CreateOrderCommand createOrderCommandFromDto(CreateOrderDTO dto) {
        final var command = new CreateOrderCommand();
        command.setId(UUID.randomUUID().toString());
        command.setUserEmail(dto.userEmail());
        command.setUserName(dto.userName());
        command.setStatus(OrderStatus.NEW);
        command.setDeliveryAddress(dto.deliveryAddress());
        command.setDeliveryDate(dto.deliveryDate());
        return command;
    }

    public static OrderCreatedEvent orderCreatedEventFromOrder(Order order) {
        return new OrderCreatedEvent(
                order.getId().toString(),
                order.getUserEmail(),
                order.getUserName(),
                order.getDeliveryAddress(),
                order.getStatus(),
                order.getDeliveryDate());
    }
}
