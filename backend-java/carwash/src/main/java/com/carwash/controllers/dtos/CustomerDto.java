package com.carwash.controllers.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
public class CustomerDto {

  private Long id;

  private String name;

  private String email;

  @Column(name = "phone")
    private String phoneNumber;
}
