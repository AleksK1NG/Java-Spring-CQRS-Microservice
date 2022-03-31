package com.microservice.order.delivery.http;

import com.microservice.order.commands.CreateOrderCommand;
import com.microservice.order.domain.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/order")
public class OrderController {

    @GetMapping
    public ResponseEntity<String> getUser(@RequestParam(name = "name", required = false, defaultValue = "Alex") String name) {
        log.info("(GetUser) name: {}", name);
        return ResponseEntity.ok(  name);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateOrderCommand> createOrder(@RequestBody CreateOrderCommand command) {
        command.setId(UUID.randomUUID().toString());
        command.setStatus(OrderStatus.NEW);
        log.info("CreateOrderCommand: {}", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(command);
    }
}
