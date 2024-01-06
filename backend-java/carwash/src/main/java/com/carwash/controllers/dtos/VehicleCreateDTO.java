package com.carwash.controllers.dtos;

import com.carwash.entities.Customer;
import com.carwash.entities.enumerations.BrandEnum;
import com.carwash.entities.enumerations.CarModelEnum;
import com.carwash.entities.enumerations.CategoryEnum;
import com.carwash.entities.enumerations.ColorEnum;
import jakarta.validation.constraints.NotNull;

public record VehicleCreateDTO(
        String licensePlate,
        @NotNull BrandEnum brand,
        @NotNull CarModelEnum model,
        @NotNull ColorEnum color,
        @NotNull CategoryEnum category,
        Customer customer
) {
}