package com.carwash.controllers;

import com.carwash.controllers.dtos.VehicleCreateDTO;
import com.carwash.controllers.dtos.VehicleReadDTO;
import com.carwash.entities.VehicleInformation;
import com.carwash.services.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vehicles/api")
@RequiredArgsConstructor
public class VehicleController {

  private final VehicleService vehicleService;

  @GetMapping("/{id}")
  public ResponseEntity<VehicleReadDTO> findVehicle(@PathVariable("id") Long vehicleId) {
    VehicleReadDTO foundVehicle = vehicleService.findById(vehicleId);
    return ResponseEntity.ok(foundVehicle);
  }

  @GetMapping("/plate")
  public ResponseEntity<VehicleReadDTO> findByLicensePlate(
          @RequestParam(required = false) String plate) {
    VehicleReadDTO vehicleReadDTO = vehicleService.findByLicensePlate(plate);
    return ResponseEntity.status(HttpStatus.OK).body(vehicleReadDTO);
  }

  @GetMapping
  public ResponseEntity<List<VehicleReadDTO>> findVehicles() {
    List<VehicleReadDTO> foundVehicles = vehicleService.findAll();
    return ResponseEntity.ok(foundVehicles);
  }

  @PostMapping
  public ResponseEntity<VehicleReadDTO> createVehicle(
          @RequestBody @Valid VehicleCreateDTO vehicleCreateDTO) {
    VehicleReadDTO createdVehicle = vehicleService.create(vehicleCreateDTO);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(createdVehicle.getId())
            .toUri();
    return ResponseEntity.created(uri).body(createdVehicle);
  }

  @PutMapping("/{id}")
  public ResponseEntity<VehicleReadDTO> updateVehicle(@PathVariable("id") Long vehicleId,
                                                      @RequestBody
                                                      @Valid VehicleCreateDTO vehicleCreateDTO) {
    VehicleReadDTO updatedVehicle = vehicleService.update(vehicleId, vehicleCreateDTO);
    return ResponseEntity.ok(updatedVehicle);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteVehicle(@PathVariable("id") Long vehicleId) {
    vehicleService.deleteVehicle(vehicleId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/api-plate")
  public ResponseEntity<VehicleInformation> getVehicleInformation(@RequestParam String licensePlate) {
    VehicleInformation vehicleInformation = vehicleService.findVehicleInformation(licensePlate);
    return ResponseEntity.status(HttpStatus.OK).body(vehicleInformation);
  }
}
