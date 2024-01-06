package com.carwash.services;

import com.carwash.controllers.dtos.VehicleCreateDTO;
import com.carwash.controllers.dtos.VehicleReadDTO;
import com.carwash.entities.Vehicle;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleReadDTO findVehicle(Long vehicleId) {
        Vehicle foundVehicle = getVehicleById(vehicleId);
        return VehicleReadDTO.getVehicleReadDTO(foundVehicle);
    }

    public List<VehicleReadDTO> findVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No vehicle was found");
        }
        return vehicles.stream().map(VehicleReadDTO::getVehicleReadDTO).collect(Collectors.toList());
    }

    public VehicleReadDTO createVehicle(VehicleCreateDTO vehicleCreateDTO) {
        Vehicle savedVehicle = getEntityFromDTO(vehicleCreateDTO);
        try {
            savedVehicle = vehicleRepository.save(savedVehicle);
            return VehicleReadDTO.getVehicleReadDTO(savedVehicle);
        } catch (Exception e) {
            throw new ResourceStorageException("Unknown problem by saving vehicle");
        }
    }

    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicleFound = getVehicleById(vehicleId);
        vehicleRepository.deleteById(vehicleFound.getId());
    }

    private Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for id = " + vehicleId));
    }

    private Vehicle getEntityFromDTO(VehicleCreateDTO vehicleCreateDTO) {
        return Vehicle.builder()
                .licensePlate(vehicleCreateDTO.licensePlate())
                .brand(vehicleCreateDTO.brand())
                .model(vehicleCreateDTO.model())
                .color(vehicleCreateDTO.color())
                .category(vehicleCreateDTO.category())
                .customer(vehicleCreateDTO.customer())
                .build();
    }
}
