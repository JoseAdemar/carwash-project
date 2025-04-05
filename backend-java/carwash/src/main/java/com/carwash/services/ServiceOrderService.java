package com.carwash.services;

import com.carwash.controllers.dtos.ServiceOrderDto;
import com.carwash.controllers.dtos.ServiceOrderVehicleDTO;
import com.carwash.controllers.dtos.VehicleReadDTO;
import com.carwash.entities.ServiceOrder;
import com.carwash.entities.Vehicle;
import com.carwash.entities.enumerations.WashStatusEnum;
import com.carwash.entities.enumerations.WashTypeEnum;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.ServiceOrderRepository;
import com.carwash.repositories.VehicleRepository;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOrderService {
  @Autowired
  private ServiceOrderRepository serviceOrderRepository;

  @Autowired
  VehicleRepository vehicleRepository;

  public List<ServiceOrderDto> findAllserviceOrder() {
    try {
      List<ServiceOrder> serviceOrders = serviceOrderRepository.findAll();
      if (serviceOrders.isEmpty()) {
        throw new ResourceNotFoundException("Nenhuma ordem de serviço encontrada");
      }
      List<ServiceOrderDto> serviceOrderDtos = new ArrayList<>();

      for (ServiceOrder serviceOrder : serviceOrders) {
        ServiceOrderDto dto = new ServiceOrderDto(serviceOrder);
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
      ServiceOrderDto serviceOrderDto = new ServiceOrderDto(serviceOrder);
      BeanUtils.copyProperties(serviceOrder, serviceOrderDto);
      return serviceOrderDto;
    } catch (ResourceStorageException e) {
      throw new ResourceStorageException(String
              .format("Problema desconhecido ao tentar encontrar a ordem de serviço para o id " +
                      id));
    }
  }

  public ServiceOrderDto update(Long id, ServiceOrderDto serviceOrderDto) {
    ServiceOrderDto getServiceOrderDto = findServiceOrderById(id);
    if (getServiceOrderDto.washStatus().equals(WashStatusEnum.FINISHED) ||
            getServiceOrderDto.washStatus().equals(WashStatusEnum.CANCELED)) {
      throw new IllegalArgumentException(
              "Não é possível editar ordem de serviço com status cancelado ou finalizado");
    }
    ServiceOrder serviceOrder = serviceOrderRepository.findById(id).get();
    BeanUtils.copyProperties(serviceOrderDto, serviceOrder, "id");
    serviceOrderRepository.save(serviceOrder);
    return serviceOrderDto;
  }

  public VehicleReadDTO findVehicleByPlate(String plate) {
    return vehicleRepository.findByLicensePlate(plate)
            .stream().map(vehicle -> VehicleReadDTO.getVehicleReadDTO(vehicle))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Placa não encontrada no sistema"));
  }

  @Transactional
  public ServiceOrderDto createServiceOrder(ServiceOrderDto serviceOrderDto) {
    List<Vehicle> vehicles = serviceOrderDto.vehicles().stream()
            .map(plate -> vehicleRepository.findByLicensePlate(plate.getLicensePlate())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Placa não encontrada" + plate)))
            .collect(Collectors.toList());
    ServiceOrder serviceOrder = new ServiceOrder(serviceOrderDto);
    serviceOrder.setId(0L);
    serviceOrder.setVehicles(vehicles);
    serviceOrderRepository.save(serviceOrder);
    return new ServiceOrderDto(serviceOrder);
  }

  public List<ServiceOrderVehicleDTO> getServiceOrderDetails() {
    List<Tuple> results = serviceOrderRepository.findServiceOrdersWithDetails();
    List<ServiceOrderVehicleDTO> dtoList = new ArrayList<>();

    // Itera sobre os resultados e converte cada Tuple para ServiceOrderVehicleDTO
    for (Tuple tuple : results) {
      // Recupera os valores da tuple
      Long orderId = tuple.get("orderId", Long.class);
      BigDecimal price = tuple.get("price", BigDecimal.class);
      String status = tuple.get("status", String.class);
      String type = tuple.get("type", String.class);

      // Converte o Timestamp para LocalDateTime, se for necessário
      Timestamp timestamp = tuple.get("date_time", Timestamp.class);
      LocalDateTime dateTime = (timestamp != null) ? timestamp.toLocalDateTime() : null;

      String model = tuple.get("model", String.class);
      String license = tuple.get("license", String.class);
      String name = tuple.get("name", String.class);

      // Cria o DTO e adiciona à lista
      ServiceOrderVehicleDTO dto = new ServiceOrderVehicleDTO(
              orderId,
              price,
              status,
              type,
              dateTime,  // Agora já é LocalDateTime
              model,
              license,
              name
      );
      dtoList.add(dto);
    }
    return dtoList;
  }

  public void updateWashStatusToFinished(Long id) {
    ServiceOrder serviceOrder = serviceOrderRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Ordem de serviço com ID " + id + " não encontrada"));
    serviceOrder.setWashStatus(WashStatusEnum.FINISHED);

    serviceOrderRepository.save(serviceOrder);

  }
}


















