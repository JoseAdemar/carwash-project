package com.carwash.entities;

import com.carwash.entities.enumerations.WashStatusEnum;
import com.carwash.entities.enumerations.WashTypeEnum;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_order")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "service_order_vehicle",
    joinColumns = @JoinColumn(name = "service_order_id"),
    inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private List<Vehicle> vehicles;

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
