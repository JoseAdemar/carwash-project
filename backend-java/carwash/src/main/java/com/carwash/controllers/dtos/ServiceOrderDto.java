package com.carwash.controllers.dtos;

import com.carwash.entities.Customer;
import com.carwash.entities.Vehicle;
import com.carwash.entities.enumerations.WashStatusEnum;
import com.carwash.entities.enumerations.WashTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderDto {

    private Long id;

    private List<Vehicle> vehicles;

    private WashStatusEnum washStatus;

    private WashTypeEnum washType;

    private BigDecimal price;

    private LocalDateTime localDateTime;
}
