package com.microservice.order.delivery.http;

import com.microservice.order.commands.ChangeDeliveryAddressCommand;
import com.microservice.order.commands.CommandsHandler;
import com.microservice.order.commands.CreateOrderCommand;
import com.microservice.order.commands.UpdateOrderStatusCommand;
import com.microservice.order.domain.OrderDocument;
import com.microservice.order.domain.OrderStatus;
import com.microservice.order.dto.OrderResponseDto;
import com.microservice.order.queries.GetOrderByIdQuery;
import com.microservice.order.queries.GetOrdersByStatusQuery;
import com.microservice.order.queries.GetOrdersByUserEmailQuery;
import com.microservice.order.queries.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final CommandsHandler commandsHandler;
    private final QueryHandler queryHandler;

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String id) {
        final var order = queryHandler.handle(new GetOrderByIdQuery(id));
        log.info("find order: {}", order);
        return ResponseEntity.ok(order);
    }

    @GetMapping(path = "/byEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OrderDocument>> getOrdersByEmail(@RequestHeader(name = "X-User-Email") String email,
                                                                @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                                @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        final var documents = queryHandler.handle(new GetOrdersByUserEmailQuery(email, page, size));
        log.info("documents: {}", documents);
        return ResponseEntity.ok(documents);
    }

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OrderDocument>> getOrdersByStatus(@RequestParam(name = "status") OrderStatus status,
                                                                 @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        final var documents = queryHandler.handle(new GetOrdersByStatusQuery(status, page, size));
        log.info("documents: {}", documents);
        return ResponseEntity.ok(documents);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        command.setId(UUID.randomUUID().toString());
        command.setStatus(OrderStatus.NEW);
        final var id = commandsHandler.handle(command);
        log.info("created order id: {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(command);
    }

    @PutMapping(path = "{id}/address", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeDeliveryAddress(@RequestBody @Valid ChangeDeliveryAddressCommand command, @PathVariable String id) {
        command.setId(id);
        commandsHandler.handle(command);
        log.info("changed address id: {}, address: {}", id, command.getDeliveryAddress());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(path = "{id}/status", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateOrderStatus(@RequestBody @Valid UpdateOrderStatusCommand command, @PathVariable String id) {
        command.setId(id);
        commandsHandler.handle(command);
        log.info("updated status id: {}, status: {}", id, command.getStatus());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
