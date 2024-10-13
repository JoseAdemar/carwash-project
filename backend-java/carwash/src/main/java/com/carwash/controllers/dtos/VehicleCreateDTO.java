package com.carwash.controllers.dtos;

import com.carwash.entities.Customer;
import jakarta.validation.constraints.NotNull;

public record VehicleCreateDTO(
        String licensePlate,
        String brand,
        String carModel,
        String color,
        Customer customer
) {
}