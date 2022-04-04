package com.microservice.order.delivery.http;

import com.microservice.mappers.OrderMapper;
import com.microservice.order.commands.ChangeDeliveryAddressCommand;
import com.microservice.order.commands.CommandsHandler;
import com.microservice.order.commands.UpdateOrderStatusCommand;
import com.microservice.order.domain.OrderStatus;
import com.microservice.order.dto.ChangeDeliveryAddressDTO;
import com.microservice.order.dto.CreateOrderDTO;
import com.microservice.order.dto.OrderResponseDto;
import com.microservice.order.dto.UpdateOrderStatusDTO;
import com.microservice.order.queries.GetOrderByIdQuery;
import com.microservice.order.queries.GetOrdersByStatusQuery;
import com.microservice.order.queries.GetOrdersByUserEmailQuery;
import com.microservice.order.queries.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final CommandsHandler commandsHandler;
    private final QueryHandler queryHandler;
    private final Tracer tracer;

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String id) {
        final var order = queryHandler.handle(new GetOrderByIdQuery(id));

        log.info("find order: {}", order);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("order", order.toString()));
        return ResponseEntity.ok(order);
    }

    @GetMapping(path = "/byEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByEmail(@RequestHeader(name = "X-User-Email") String email,
                                                                   @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                                   @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        final var documents = queryHandler.handle(new GetOrdersByUserEmailQuery(email, page, size));

        log.info("documents: {}", documents);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("documents", documents.toString()));
        return ResponseEntity.ok(documents);
    }

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByStatus(@RequestParam(name = "status") OrderStatus status,
                                                                    @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                                    @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        final var documents = queryHandler.handle(new GetOrdersByStatusQuery(status, page, size));
        log.info("documents: {}", documents);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("documents", documents.toString()));
        return ResponseEntity.ok(documents);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrder(@Valid @RequestBody CreateOrderDTO dto) {
        final var command = OrderMapper.createOrderCommandFromDto(dto);
        final var id = commandsHandler.handle(command);

        log.info("created order id: {}", id);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("id", id));
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping(path = "{id}/address", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeDeliveryAddress(@RequestBody @Valid ChangeDeliveryAddressDTO dto, @PathVariable String id) {
        final var command = new ChangeDeliveryAddressCommand();
        command.setDeliveryAddress(dto.deliveryAddress());
        command.setId(id);
        commandsHandler.handle(command);

        log.info("changed address order id: {}, address: {}", id, command.getDeliveryAddress());
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("id", id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(path = "{id}/status", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateOrderStatus(@RequestBody @Valid UpdateOrderStatusDTO dto, @PathVariable String id) {
        final var command = new UpdateOrderStatusCommand();
        command.setId(id);
        command.setStatus(dto.status());
        commandsHandler.handle(command);

        log.info("updated status order id: {}, status: {}", id, command.getStatus());
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.tag("id", id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
