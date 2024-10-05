package com.carwash.services;

import com.carwash.entities.enumerations.BrandEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BrandService {
  public List<BrandEnum> brandEnum () {
   return Arrays.asList(BrandEnum.values());
  }
}
