package com.carwash.controllers;

import com.carwash.entities.enumerations.CategoryEnum;
import com.carwash.entities.enumerations.ColorEnum;
import com.carwash.services.CategoryService;
import com.carwash.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/colors")
@RequiredArgsConstructor
public class ColorController {
  @Autowired
  private ColorService colorService;

  @GetMapping
  public ResponseEntity<List<ColorEnum>> colors() {
    List<ColorEnum> colorEnumList = colorService.colors();
    return ResponseEntity.ok(colorEnumList);
  }
}