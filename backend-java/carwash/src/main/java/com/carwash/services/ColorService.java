package com.carwash.services;

import com.carwash.entities.enumerations.ColorEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ColorService {
  public List<ColorEnum> colors() {
    return Arrays.asList(ColorEnum.values());
  }
}
