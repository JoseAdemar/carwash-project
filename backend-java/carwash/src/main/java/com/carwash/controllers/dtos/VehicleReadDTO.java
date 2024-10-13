package com.carwash.controllers.dtos;

import com.carwash.entities.Customer;
import com.carwash.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleReadDTO {

    private Long id;
    private String licensePlate;
    private String brand;
    private String carModel;
    private String color;
    private Customer customer;

    public static VehicleReadDTO getVehicleReadDTO(Vehicle vehicle) {
        return VehicleReadDTO.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .brand(vehicle.getBrand())
                .color(vehicle.getColor())
                .carModel(vehicle.getCarModel())
                .customer(vehicle.getCustomer())
                .build();
    }
}
