package com.microservice.order.commands;

import com.microservice.order.domain.OrderStatus;
import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.order.mappers.OrderMapper;
import com.microservice.order.repository.OrderPostgresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderCommandsHandler implements CommandsHandler {
    private final OrderPostgresRepository postgresRepository;

    @Override
    public String handle(CreateOrderCommand command) {
        final var order = OrderMapper.orderFromCreateOrderCommand(command);
        final var savedOrder = postgresRepository.save(order);
        log.info("savedOrder: {}", savedOrder);
        return savedOrder.getId().toString();
    }

    @Override
    @Transactional
    public void handle(UpdateOrderStatusCommand command) {
        final var orderOptional = postgresRepository.findById(UUID.fromString(command.getId()));
        if (orderOptional.isEmpty()) throw new OrderNotFoundException("order not found: " + command.getId());

        final var order = orderOptional.get();
        order.setStatus(OrderStatus.valueOf(command.getStatus().toString()));
        order.setUpdatedAt(ZonedDateTime.now());
        postgresRepository.save(order);
    }

    @Override
    @Transactional
    public void handle(ChangeDeliveryAddressCommand command) {
        final var orderOptional = postgresRepository.findById(UUID.fromString(command.getId()));
        if (orderOptional.isEmpty()) throw new OrderNotFoundException("order not found: " + command.getId());

        final var order = orderOptional.get();
        order.setDeliveryAddress(command.getDeliveryAddress());
        order.setUpdatedAt(ZonedDateTime.now());
        postgresRepository.save(order);
    }

}
