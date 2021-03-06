package com.microservice.order.commands;

import com.microservice.order.domain.OrderStatus;
import com.microservice.shared.commands.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UpdateOrderStatusCommand extends BaseCommand {
    @NotNull
    private OrderStatus status;
}
