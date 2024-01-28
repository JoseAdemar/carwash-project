package com.carwash.controllers;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CustomerDto> getCustomerDtoById(@PathVariable("id") Long id) {
        CustomerDto customerDto = customerService.getCustomerDtoById(id);
        if (customerDto != null) {
            return ResponseEntity.ok(customerDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CustomerDto> saveCustomerDto(@RequestBody CustomerDto customerDto) {
        customerService.saveCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomerDto(@PathVariable("id") Long id, @RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(id, customerDto);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDto> deleteCustomerDto(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
