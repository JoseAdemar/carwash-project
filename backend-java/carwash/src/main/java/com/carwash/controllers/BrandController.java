package com.carwash.controllers;

import com.carwash.entities.enumerations.BrandEnum;
import com.carwash.services.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/brands")
@RequiredArgsConstructor
public class BrandController {
  @Autowired
  private BrandService brandService;

  @GetMapping
  public ResponseEntity<List<BrandEnum>> brands() {
    List<BrandEnum> brandEnumList = brandService.brandEnum();
     return ResponseEntity.ok(brandEnumList);
  }
}
