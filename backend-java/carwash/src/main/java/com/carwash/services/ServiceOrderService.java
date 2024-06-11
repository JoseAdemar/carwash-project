package com.carwash.services;

import com.carwash.controllers.dtos.ServiceOrderDto;
import com.carwash.entities.ServiceOrder;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.ServiceOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOrderService {
  @Autowired
    private ServiceOrderRepository serviceOrderRepository;

  @Transactional
    public ServiceOrderDto createServiceOrderDto(ServiceOrderDto serviceOrderDto) {
    try {
      ServiceOrder serviceOrder = new ServiceOrder();
      BeanUtils.copyProperties(serviceOrderDto, serviceOrder);
      serviceOrderRepository.save(serviceOrder);
      return serviceOrderDto;
    } catch (ResourceStorageException e) {
      throw new ResourceStorageException(String.format("Unknown problem by saving vehicle"));
    }
  }

  public List<ServiceOrderDto> findAllserviceOrder() {
    try {
      List<ServiceOrder> serviceOrders = serviceOrderRepository.findAll();
      if (serviceOrders.isEmpty()) {
        throw new ResourceNotFoundException("No service order was found");
      }
      List<ServiceOrderDto> serviceOrderDtos = new ArrayList<>();

      for (ServiceOrder serviceOrder : serviceOrders) {
        ServiceOrderDto dto = new ServiceOrderDto();
        BeanUtils.copyProperties(serviceOrder, dto);
        serviceOrderDtos.add(dto);
      }
      return serviceOrderDtos;
    } catch (ResourceStorageException e) {
      throw new ResourceStorageException(String
              .format("Unknown problem by searching  service order"));
    }
  }

  public ServiceOrderDto findServiceOrderById(Long id) {
    try {
      ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
          .orElseThrow(() ->
                new ResourceNotFoundException("Not found service order with Id = " + id));
      ServiceOrderDto serviceOrderDto = new ServiceOrderDto();
      BeanUtils.copyProperties(serviceOrder, serviceOrderDto);
      return serviceOrderDto;
    } catch (ResourceStorageException e) {
      throw new ResourceStorageException(String
                    .format("Unknown problem by searching service order by Id " + id));
    }
  }
}
