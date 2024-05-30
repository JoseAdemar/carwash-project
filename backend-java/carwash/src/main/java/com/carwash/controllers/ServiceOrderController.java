package com.carwash.controllers;

import com.carwash.controllers.dtos.ServiceOrderDto;
import com.carwash.entities.ServiceOrder;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.services.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/serviceorder")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @PostMapping
    public ResponseEntity<?> saveServiceOrder(@RequestBody ServiceOrderDto serviceOrderDto) {
        try {
            ServiceOrderDto dto = serviceOrderService.createServiceOrderDto(serviceOrderDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (ResourceStorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAllServiceOrder() {
        try {
            List<ServiceOrderDto> serviceOrderDtos = serviceOrderService.findAllserviceOrder();
            return ResponseEntity.status(HttpStatus.OK).body(serviceOrderDtos);
        } catch (ResourceStorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOrderServiceById(@PathVariable("id") Long id) {
        try {
            ServiceOrderDto serviceOrderDto = serviceOrderService.findServiceOrderById(id);
            return ResponseEntity.status(HttpStatus.OK).body(serviceOrderDto);
        } catch (ResourceStorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
