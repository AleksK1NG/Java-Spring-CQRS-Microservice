package com.microservice.order.delivery.http;

import com.microservice.order.commands.CommandsHandler;
import com.microservice.order.commands.CreateOrderCommand;
import com.microservice.order.domain.OrderStatus;
import com.microservice.order.dto.OrderResponseDto;
import com.microservice.order.queries.GetOrderByIdQuery;
import com.microservice.order.queries.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final CommandsHandler commandsHandler;
    private final QueryHandler queryHandler;

    @GetMapping(path = "{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String id) {
        log.info("id: {}", id);
        final var order = queryHandler.handle(new GetOrderByIdQuery(id));
        log.info("order: {}", order);
        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderCommand command) {
        command.setId(UUID.randomUUID().toString());
        command.setStatus(OrderStatus.NEW);
        log.info("CreateOrderCommand: {}", command);
        final var id = commandsHandler.handle(command);
        log.info("CreateOrderCommand id: {}", id);

        if (!id.equals(command.getId())) {
            log.error("invalid id");
            return ResponseEntity.internalServerError().body("invalid id");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(command);
    }


}
