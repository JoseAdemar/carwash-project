package com.carwash.controllers.dto;

import com.carwash.controllers.dto.dto.CustomerDto;
import com.carwash.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.findAllCustomer();
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerDtoById(@PathVariable("id") Long id){
       return customerService.getCustomerDtoById(id);
    }
}
