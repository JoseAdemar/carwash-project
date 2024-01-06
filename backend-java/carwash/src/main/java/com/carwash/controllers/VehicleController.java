package com.carwash.controllers;

import com.carwash.controllers.dtos.VehicleCreateDTO;
import com.carwash.controllers.dtos.VehicleReadDTO;
import com.carwash.services.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/{id}")
    public ResponseEntity<VehicleReadDTO> findVehicle(@PathVariable("id") Long vehicleId){
        VehicleReadDTO foundVehicle = vehicleService.findVehicle(vehicleId);
        return ResponseEntity.ok(foundVehicle);
    }

    @GetMapping
    public ResponseEntity<List<VehicleReadDTO>> findVehicles(){
        List<VehicleReadDTO> foundVehicles = vehicleService.findVehicles();
        return ResponseEntity.ok(foundVehicles);
    }

    @PostMapping
    public ResponseEntity<VehicleReadDTO> createVehicle(@RequestBody @Valid VehicleCreateDTO vehicleCreateDTO) {
        VehicleReadDTO createdVehicle = vehicleService.createVehicle(vehicleCreateDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdVehicle.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("id") Long vehicleId){
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }
}
