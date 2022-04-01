package com.microservice.order.events;

public record OrderDeliveryAddressChangedEvent(String id,String deliveryAddress) {
}
