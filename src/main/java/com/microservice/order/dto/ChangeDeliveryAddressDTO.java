package com.microservice.order.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record ChangeDeliveryAddressDTO(
        @NotBlank
        @Size(min = 10, message = "delivery address should be at least 10 characters")
        @Size(max = 500, message = "delivery address should not be greater than 500 characters") String deliveryAddress) {
}
