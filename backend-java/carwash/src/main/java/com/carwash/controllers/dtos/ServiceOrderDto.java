package com.carwash.controllers.dtos;

import com.carwash.entities.Customer;
import com.carwash.entities.Vehicle;
import com.carwash.entities.enumerations.WashStatusEnum;
import com.carwash.entities.enumerations.WashTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class ServiceOrderDto {

    private CustomerDto customer;

    private Set<VehicleCreateDTO> vehicles;

    private WashStatusEnum washStatus;

    private WashTypeEnum washType;

    private BigDecimal bigDecimal;
}
