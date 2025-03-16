package com.carwash.controllers.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceOrderVehicleDTO(Long serviceOrderId,
                                     BigDecimal price,
                                     String washStatus,
                                     String washType,
                                     LocalDateTime date,
                                     String carModel,
                                     String license,
                                     String customerName) {
}
