package com.carwash.services;

import com.carwash.entities.enumerations.CategoryEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
  public List<CategoryEnum> categories() {
    return Arrays.asList(CategoryEnum.values());
  }
}
