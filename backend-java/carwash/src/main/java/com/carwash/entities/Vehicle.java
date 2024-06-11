package com.carwash.entities;

import com.carwash.entities.enumerations.BrandEnum;
import com.carwash.entities.enumerations.CarModelEnum;
import com.carwash.entities.enumerations.CategoryEnum;
import com.carwash.entities.enumerations.ColorEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicle")
@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
public class Vehicle {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "license")
  private String licensePlate;

  @Enumerated(EnumType.STRING)
  @NotNull
  private BrandEnum brand;

  @Enumerated(EnumType.STRING)
  @NotNull
  private CarModelEnum model;

  @Enumerated(EnumType.STRING)
  @NotNull
  private ColorEnum color;

  @Enumerated(EnumType.STRING)
  @NotNull
  private CategoryEnum category;

  @ManyToOne
  private Customer customer;

}
