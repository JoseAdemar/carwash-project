package com.carwash.entities;

import com.carwash.entities.enumerations.WashStatusEnum;
import com.carwash.entities.enumerations.WashTypeEnum;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "service_order_vehicle",
    joinColumns = @JoinColumn(name = "service_order_id"),
    inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> vehicles;

    @Column(name = "date")
    @CreationTimestamp
    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    private WashStatusEnum washStatus;

    @Enumerated(EnumType.STRING)
    private WashTypeEnum washType;

    private BigDecimal bigDecimal;
}
