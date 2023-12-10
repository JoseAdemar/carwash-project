package com.carwash.entity;

import com.carwash.enumeration.BrandEnum;
import com.carwash.enumeration.CarModelEnum;
import com.carwash.enumeration.CategoryEnum;
import com.carwash.enumeration.ColorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Data
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
}
