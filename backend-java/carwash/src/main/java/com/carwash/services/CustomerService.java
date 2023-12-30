package com.carwash.services;

import com.carwash.controllers.dto.dto.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.repositories.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

     @Autowired
     private CustomerRepository customerRepository;

     public List<CustomerDto> findAllCustomer(){
          List<Customer> customers = customerRepository.findAll();
          return mapToCustomerDto(customers);
     }

     public CustomerDto getCustomerDtoById(Long id){
          Customer customer = customerRepository.findById(id).get();
          CustomerDto customerDto = new CustomerDto();
          BeanUtils.copyProperties(customer, customerDto);
          return customerDto;
     }

     private List<CustomerDto> mapToCustomerDto(List<Customer> customers) {
          return customers.stream().map(customer -> {
               CustomerDto customerDto = new CustomerDto();
               BeanUtils.copyProperties(customer, customerDto);
               return customerDto;
          }).collect(Collectors.toList());
     }
}
