package com.carwash.controllers.dtos;

import com.carwash.entities.ServiceOrder;
import com.carwash.entities.Vehicle;
import com.carwash.entities.enumerations.WashStatusEnum;
import com.carwash.entities.enumerations.WashTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ServiceOrderDto(
        Long id,
        List<Vehicle> vehicles,
        WashStatusEnum washStatus,
        WashTypeEnum washType,
        BigDecimal price,
        LocalDateTime localDateTime
) {
  public ServiceOrderDto(ServiceOrder serviceOrder) {
    this(serviceOrder.getId(),
            serviceOrder.getVehicles(),
            serviceOrder.getWashStatus(),
            serviceOrder.getWashType(),
            serviceOrder.getPrice(),
            serviceOrder.getLocalDateTime());
  }

}
