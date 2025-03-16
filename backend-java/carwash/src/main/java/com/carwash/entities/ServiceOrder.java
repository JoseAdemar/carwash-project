package com.carwash.entities;

import com.carwash.controllers.dtos.ServiceOrderDto;
import com.carwash.entities.enumerations.WashStatusEnum;
import com.carwash.entities.enumerations.WashTypeEnum;

import jakarta.persistence.*;

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

  @ManyToMany
  @JoinTable(
          name = "service_order_vehicle",
          joinColumns = @JoinColumn(name = "service_order_id"),
          inverseJoinColumns = @JoinColumn(name = "vehicles_id")
  )
  private List<Vehicle> vehicles;

  @Column(name = "date")
  @CreationTimestamp
  private LocalDateTime localDateTime;

  @Enumerated(EnumType.STRING)
  private WashStatusEnum washStatus = WashStatusEnum.WASHING;

  @Enumerated(EnumType.STRING)
  private WashTypeEnum washType = WashTypeEnum.NORMAL;

  private BigDecimal price;

  public ServiceOrder(ServiceOrderDto serviceOrderDto){
    this.id = serviceOrderDto.id();
    this.vehicles = serviceOrderDto.vehicles();
    this.washStatus = serviceOrderDto.washStatus();
    this.washType = serviceOrderDto.washType();
    this.price = serviceOrderDto.price();
    this.localDateTime = serviceOrderDto.localDateTime();
  }
}
