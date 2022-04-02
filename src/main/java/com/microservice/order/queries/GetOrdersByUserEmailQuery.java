package com.microservice.order.queries;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record GetOrdersByUserEmailQuery(
        @NotBlank @Size(min = 5, max = 250, message = "invalid email length") String userEmail,
        int page,
        int size) {

}
