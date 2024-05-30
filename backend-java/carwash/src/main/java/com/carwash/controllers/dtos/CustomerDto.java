package com.carwash.controllers.dtos;

import com.carwash.entities.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long id;

    private String name;

    private String email;

    @Column(name = "phone")
    private String phoneNumber;
}
