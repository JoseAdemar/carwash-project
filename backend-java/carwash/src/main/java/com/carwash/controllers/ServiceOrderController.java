package com.carwash.controllers;

import com.carwash.controllers.dtos.ServiceOrderDto;
import com.carwash.services.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/serviceorder")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @PostMapping
    public ResponseEntity<ServiceOrderDto> saveServiceOrder(@RequestBody ServiceOrderDto serviceOrderDto){
        ServiceOrderDto dto = serviceOrderService.saveServiceOrder(serviceOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
