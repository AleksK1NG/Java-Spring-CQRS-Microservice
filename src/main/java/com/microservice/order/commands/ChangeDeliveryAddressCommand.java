package com.microservice.order.commands;

import com.microservice.shared.commands.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ChangeDeliveryAddressCommand extends BaseCommand {
    @NotBlank
    @Size(min=10, message = "deliveryAddress should be atleast 10 characters")
    @Size(max=500, message = "deliveryAddress should not be greater than 500 characters")
    private String deliveryAddress;
}
