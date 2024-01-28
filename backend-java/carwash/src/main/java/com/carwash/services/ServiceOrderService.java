package com.carwash.services;

import com.carwash.controllers.dtos.ServiceOrderDto;
import com.carwash.entities.ServiceOrder;
import com.carwash.repositories.ServiceOrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    public ServiceOrderDto saveServiceOrder(ServiceOrderDto serviceOrderDto){
        ServiceOrder serviceOrder = new ServiceOrder();
        BeanUtils.copyProperties(serviceOrderDto, serviceOrder);
        serviceOrderRepository.save(serviceOrder);
        return serviceOrderDto;
    }

}
