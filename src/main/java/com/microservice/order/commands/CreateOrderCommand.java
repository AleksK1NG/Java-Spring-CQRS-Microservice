package com.microservice.order.commands;

import com.microservice.order.domain.OrderStatus;
import com.microservice.shared.commands.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CreateOrderCommand extends BaseCommand {
    @NotBlank
    @Size(min = 5,max = 250, message = "invalid userEmail length")
    private String userEmail;

    @NotBlank
    @Size(min = 5,max = 250, message = "invalid userName length")
    private String userName;

    @NotBlank
    @Size(min = 5,max = 250, message = "invalid deliveryAddress length")
    private String deliveryAddress;


    private OrderStatus status;

    @NotNull
    private LocalDateTime deliveryDate;
}
