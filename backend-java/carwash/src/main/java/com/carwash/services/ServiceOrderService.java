package com.carwash.services;

import com.carwash.controllers.dtos.ServiceOrderDto;
import com.carwash.entities.ServiceOrder;
import com.carwash.entities.enumerations.WashStatusEnum;
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
      throw new ResourceStorageException(
              String.format("Não foi possível criar a ordem de serviço"));
    }
  }

  public List<ServiceOrderDto> findAllserviceOrder() {
    try {
      List<ServiceOrder> serviceOrders = serviceOrderRepository.findAll();
      if (serviceOrders.isEmpty()) {
        throw new ResourceNotFoundException("Nenhuma ordem de serviço encontrada");
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
              .format("Não foi possível encontrar ordens de serviços"));
    }
  }

  public ServiceOrderDto findServiceOrderById(Long id) {
    try {
      ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
              .orElseThrow(() ->
                      new ResourceNotFoundException(
                              "Não foi encontrada ordem de serviço para o id = " + id));
      ServiceOrderDto serviceOrderDto = new ServiceOrderDto();
      BeanUtils.copyProperties(serviceOrder, serviceOrderDto);
      return serviceOrderDto;
    } catch (ResourceStorageException e) {
      throw new ResourceStorageException(String
              .format("Problema desconhecido ao tentar encontrar a ordem de serviço para o id " +
                      id));
    }
  }

  public ServiceOrderDto update( Long id, ServiceOrderDto serviceOrderDto) {
    ServiceOrderDto getServiceOrderDto = findServiceOrderById(id);
    if (getServiceOrderDto.getWashStatus().equals(WashStatusEnum.FINISHED) ||
            getServiceOrderDto.getWashStatus().equals(WashStatusEnum.CANCELED)){
      throw new IllegalArgumentException("Não é possível editar ordem de serviço com status cancelado ou finalizado");
    }
      ServiceOrder serviceOrder = serviceOrderRepository.findById(id).get();
      BeanUtils.copyProperties(serviceOrderDto, serviceOrder, "id");
      serviceOrderRepository.save(serviceOrder);
      return serviceOrderDto;
  }
}


















