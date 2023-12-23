package com.carwash.entities;

import com.carwash.entities.enumerations.BrandEnum;
import com.carwash.entities.enumerations.CarModelEnum;
import com.carwash.entities.enumerations.CategoryEnum;
import com.carwash.entities.enumerations.ColorEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicle")
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
