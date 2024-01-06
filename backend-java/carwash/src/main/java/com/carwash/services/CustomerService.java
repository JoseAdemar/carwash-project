package com.carwash.services;

import com.carwash.controllers.dto.dto.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.repositories.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

     @Autowired
     private CustomerRepository customerRepository;

     public List<CustomerDto> findAllCustomer(){
          List<Customer> customers = customerRepository.findAll();
          return mapToCustomerDto(customers);
     }

     private List<CustomerDto> mapToCustomerDto(List<Customer> customers) {
          return customers.stream().map(customer -> {
               CustomerDto customerDto = new CustomerDto();
               BeanUtils.copyProperties(customer, customerDto);
               return customerDto;
          }).collect(Collectors.toList());
     }

    public CustomerDto getCustomerDtoById(Long id) {
        try {
            Customer customer = customerRepository.findById(id).orElseThrow(NoSuchElementException::new);
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

     public void saveCustomer(CustomerDto customerDto) {
         Customer customer = new Customer();
         BeanUtils.copyProperties(customerDto, customer);
         Customer customerToSave = customerRepository.save(customer);
     }

     public void updateCustomer(Long id, CustomerDto customerDto) {
         Optional<Customer> currentCustomer = customerRepository.findById(id);
         if(currentCustomer.isPresent()){
              BeanUtils.copyProperties(customerDto, currentCustomer.get(), "id");
              customerRepository.save(currentCustomer.get());
         }
     }

    public void deleteCustomer(Long id) {
         try {
             Customer customer = customerRepository.findById(id).orElseThrow(NoSuchElementException::new);
             customerRepository.deleteById(customer.getId());
         }catch (NoSuchElementException ex){
         }
    }
}
