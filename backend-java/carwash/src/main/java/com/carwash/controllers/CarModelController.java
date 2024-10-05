package com.carwash.controllers;

import com.carwash.entities.enumerations.BrandEnum;
import com.carwash.entities.enumerations.CarModelEnum;
import com.carwash.services.CarModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/car-models")
@RequiredArgsConstructor
public class CarModelController {
  @Autowired
  private CarModelService carModelService;

  @GetMapping
  public ResponseEntity<List<CarModelEnum>> carModels() {
    List<CarModelEnum> carModelEnumList = carModelService.carModels();
    return ResponseEntity.ok(carModelEnumList);
  }
}