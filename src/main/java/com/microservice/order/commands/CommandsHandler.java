package com.microservice.order.commands;

public interface CommandsHandler {
    public String handle(CreateOrderCommand command);
}
