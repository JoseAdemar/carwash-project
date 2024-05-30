package com.carwash.services;

import com.carwash.controllers.dtos.VehicleCreateDTO;
import com.carwash.controllers.dtos.VehicleReadDTO;
import com.carwash.entities.Vehicle;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleReadDTO findVehicle(Long vehicleId) {
        Vehicle foundVehicle = getVehicleById(vehicleId);
        return VehicleReadDTO.getVehicleReadDTO(foundVehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleReadDTO> findVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No vehicle was found");
        }
        return vehicles.stream().map(VehicleReadDTO::getVehicleReadDTO).collect(Collectors.toList());
    }

    @Transactional
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

    @Transactional(readOnly = true)
    private Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException
                ("Vehicle not found for id = " + vehicleId));
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

    public VehicleReadDTO updateVehicle(Long vehicleId, VehicleCreateDTO vehicleCreateDTO) {
        Vehicle foundVehicle = getVehicleById(vehicleId);
        copyFromDTOToEntity(foundVehicle, vehicleCreateDTO);
        foundVehicle = vehicleRepository.save(foundVehicle);
        return VehicleReadDTO.getVehicleReadDTO(foundVehicle);
    }

    private void copyFromDTOToEntity(Vehicle foundVehicle, VehicleCreateDTO vehicleCreateDTO) {
        if (Objects.nonNull(vehicleCreateDTO.licensePlate())) {
            foundVehicle.setLicensePlate(vehicleCreateDTO.licensePlate());
        }
        if (Objects.nonNull(vehicleCreateDTO.brand())) {
            foundVehicle.setBrand(vehicleCreateDTO.brand());
        }
        if (Objects.nonNull(vehicleCreateDTO.model())) {
            foundVehicle.setModel(vehicleCreateDTO.model());
        }
        if (Objects.nonNull(vehicleCreateDTO.color())) {
            foundVehicle.setColor(vehicleCreateDTO.color());
        }
        if (Objects.nonNull(vehicleCreateDTO.category())) {
            foundVehicle.setCategory(vehicleCreateDTO.category());
        }
        if (Objects.nonNull(vehicleCreateDTO.customer())) {
            foundVehicle.setCustomer(vehicleCreateDTO.customer());
        }
    }
}
