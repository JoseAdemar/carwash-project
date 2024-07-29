package com.carwash.services;

import com.carwash.controllers.dtos.VehicleCreateDTO;
import com.carwash.controllers.dtos.VehicleReadDTO;
import com.carwash.entities.Vehicle;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
public class VehicleService {

  @Autowired
  private final VehicleRepository vehicleRepository;

  @Transactional(readOnly = true)
  public VehicleReadDTO findById(Long id) {
    try {
      Vehicle vehicle = vehicleRepository.findById(id)
              .orElseThrow(()
                      -> new ResourceNotFoundException("Veículo não encontrado para o Id = " + id));
      return VehicleReadDTO.getVehicleReadDTO(vehicle);
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  public VehicleReadDTO findByLicensePlate(String plate) {
    try {
      Objects.requireNonNull(plate);
      Optional<Vehicle> vehicle = (vehicleRepository.findByLicensePlate(plate));
      return VehicleReadDTO.getVehicleReadDTO(vehicle.get());
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @Transactional(readOnly = true)
  public List<VehicleReadDTO> findAll() {
    try {
      List<Vehicle> vehicles = vehicleRepository.findAll();
      if (vehicles.isEmpty()) {
        throw new ResourceNotFoundException("Nenhum veículo encontrado na busca");
      }
      return vehicles.stream().map(VehicleReadDTO::getVehicleReadDTO).collect(Collectors.toList());
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @Transactional
  public VehicleReadDTO create(VehicleCreateDTO vehicleCreateDto) {
    Vehicle vehicle = getEntityFromDto(vehicleCreateDto);
    try {
      vehicleRepository.save(vehicle);
      return VehicleReadDTO.getVehicleReadDTO(vehicle);
    } catch (ResourceStorageException e) {
      throw new ResponseStatusException(HttpStatus
              .INTERNAL_SERVER_ERROR,
              new ResourceStorageException(String.format("Erro interno ao tentar criar veículo"))
                      .getMessage());
    }
  }

  private Vehicle getEntityFromDto(VehicleCreateDTO vehicleCreateDto) {
    Vehicle vehicle = new Vehicle();
    BeanUtils.copyProperties(vehicleCreateDto, vehicle);
    return vehicle;
  }

  public void deleteVehicle(Long id) {
    try {
      Vehicle vehicleFound = vehicleRepository.findById(id).orElseThrow(() -> {
        throw new ResourceNotFoundException("Veículo não encontrado para o Id = " + id);
      });
      vehicleRepository.deleteById(vehicleFound.getId());
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  public VehicleReadDTO update(Long id, VehicleCreateDTO vehicleCreateDtO) {
    try {
      Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> {
        throw new ResourceNotFoundException("Veículo não encontrado para o Id = " + id);
      });
      BeanUtils.copyProperties(vehicleCreateDtO, vehicle);
      vehicle = vehicleRepository.save(vehicle);
      return VehicleReadDTO.getVehicleReadDTO(vehicle);
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }
}
