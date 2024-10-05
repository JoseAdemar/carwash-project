package com.carwash.services;

import com.carwash.entities.enumerations.CarModelEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CarModel {
  public List<CarModelEnum> carModels() {
    return Arrays.asList(CarModelEnum.values());
  }
}
