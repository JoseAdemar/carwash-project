package com.carwash.entity;

import com.carwash.enumeration.WashStatusEnum;
import com.carwash.enumeration.WashTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(mappedBy = "customer_id")
    private Customer customer;

    @Column(name = "date")
    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WashStatusEnum washStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WashTypeEnum washType;

    @NotNull
    private BigDecimal bigDecimal;
}
