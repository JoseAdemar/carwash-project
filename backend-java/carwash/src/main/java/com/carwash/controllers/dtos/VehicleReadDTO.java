package com.carwash.controllers.dtos;

import com.carwash.entities.Customer;
import com.carwash.entities.Vehicle;
import com.carwash.entities.enumerations.BrandEnum;
import com.carwash.entities.enumerations.CarModelEnum;
import com.carwash.entities.enumerations.CategoryEnum;
import com.carwash.entities.enumerations.ColorEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleReadDTO {

    private Long id;
    private String licensePlate;
    private BrandEnum brand;
    private CarModelEnum model;
    private ColorEnum color;
    private CategoryEnum category;
    private Customer customer;

    public static VehicleReadDTO getVehicleReadDTO(Vehicle vehicle) {
        return VehicleReadDTO.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .brand(vehicle.getBrand())
                .color(vehicle.getColor())
                .category(vehicle.getCategory())
                .customer(vehicle.getCustomer())
                .build();
    }
}
