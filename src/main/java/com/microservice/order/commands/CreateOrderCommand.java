package com.microservice.order.commands;

import com.microservice.order.domain.OrderStatus;
import com.microservice.shared.commands.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CreateOrderCommand extends BaseCommand {
    @NotBlank
    @Min(value = 10L, message = "invalid min length")
    @Max(value = 500L, message = "max length is 500")
    private String userEmail;

    @NotBlank
    @Min(value = 10L, message = "invalid min length")
    @Max(value = 500L, message = "max length is 500")
    private String userName;

    @NotBlank
    @Min(value = 10L, message = "invalid min length")
    @Max(value = 500L, message = "max length is 500")
    private String deliveryAddress;

    @NotNull
    private OrderStatus status;

    @NotNull
    private ZonedDateTime deliveryDate;
}
