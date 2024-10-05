package com.carwash.services;

import com.carwash.entities.enumerations.CategoryEnum;

import java.util.Arrays;
import java.util.List;

public class Category {
  public List<CategoryEnum> categories() {
    return Arrays.asList(CategoryEnum.values());
  }
}
