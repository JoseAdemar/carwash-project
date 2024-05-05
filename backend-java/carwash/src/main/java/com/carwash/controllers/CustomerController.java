package com.carwash.controllers;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.services.CustomerService;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<CustomerDto> customerDto = customerService.findAllCustomer();
            return ResponseEntity.ok(customerDto);
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerDtoById(@PathVariable("id") Long id) {
        try {
            CustomerDto customerDto = customerService.getCustomerDtoById(id);
            return ResponseEntity.ok(customerDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> saveCustomerDto(@RequestBody CustomerDto customerDto) {
        try {
            customerService.saveCustomer(customerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
        } catch (ResourceStorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomerDto(@PathVariable("id") Long id, @RequestBody CustomerDto customerDto) {
        try {
            customerService.updateCustomer(id, customerDto);
            return ResponseEntity.status(HttpStatus.OK).body(customerDto);
        } catch (ResourceStorageException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerDto(@PathVariable("id") Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
