package com.carwash.controllers;

import com.carwash.controllers.dtos.ServiceOrderDto;
import com.carwash.controllers.dtos.ServiceOrderVehicleDTO;
import com.carwash.controllers.dtos.VehicleReadDTO;
import com.carwash.entities.ServiceOrder;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.services.ServiceOrderService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/serviceorders/api")
public class ServiceOrderController {

  @Autowired
  private ServiceOrderService serviceOrderService;

 /* @PostMapping
  public ResponseEntity<?> saveServiceOrder(@RequestBody ServiceOrderDto serviceOrderDto) {
      ServiceOrderDto dto = serviceOrderService.createServiceOrderDto(serviceOrderDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }*/

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

  @PutMapping("/{id}")
  public ResponseEntity<ServiceOrderDto> updateServiceOrder
          (@PathVariable("id") Long id, @RequestBody ServiceOrderDto serviceOrderDto) {
    ServiceOrderDto update = serviceOrderService.update(id, serviceOrderDto);
    return ResponseEntity.status(HttpStatus.OK).body(update);
  }

  @GetMapping("/plate/{plate}")
  public ResponseEntity<?> findVehicleByPlate(@PathVariable("plate") String plate) {
    VehicleReadDTO vehicleReadDTO = serviceOrderService.findVehicleByPlate(plate);
    return ResponseEntity.status(HttpStatus.OK).body(vehicleReadDTO);
  }

  @PostMapping("/create")
  public ResponseEntity<ServiceOrderDto> createServiceOrder(
          @RequestBody ServiceOrderDto serviceOrderDto,
          UriComponentsBuilder uriBuilder) {
    ServiceOrderDto serviceOrder = serviceOrderService.createServiceOrder(serviceOrderDto);
    var uri = uriBuilder.path("/serviceorders/api/create/{id}").buildAndExpand(serviceOrder
            .id()).toUri();
    return ResponseEntity.created(uri).body(serviceOrder);
  }

  @GetMapping("/service_order_details")
  public ResponseEntity<List<ServiceOrderVehicleDTO>> getServiceOrderDetails(){
    List<ServiceOrderVehicleDTO> dtos = serviceOrderService.getServiceOrderDetails();
    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }
}

