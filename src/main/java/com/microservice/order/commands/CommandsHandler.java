package com.microservice.order.commands;

public interface CommandsHandler {
    public String handle(CreateOrderCommand command);
    public void handle(UpdateOrderStatusCommand command);
    public void handle(ChangeDeliveryAddressCommand command);
}
