package com.carwash.controllers;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
@RestController
@RequestMapping("/customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @GetMapping
  public ResponseEntity<List<CustomerDto>> getAll() {
      List<CustomerDto> customerDto = customerService.findAll();
      return ResponseEntity.ok(customerDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerDto> getById(@PathVariable("id") Long id) {
    CustomerDto customerDto = customerService.getById(id);
    return ResponseEntity.ok(customerDto);
  }

  @GetMapping("/search")
  public ResponseEntity<Optional<CustomerDto>> findByCriteria(
          @RequestParam(required = false) Long id,
          @RequestParam(required = false) String name,
          @RequestParam(required = false) String email
  ) {
    Optional<CustomerDto> customerDto = customerService.findByCriteria(id, name, email);
    return ResponseEntity.status(HttpStatus.OK).body(customerDto);
  }

  @PostMapping
  public ResponseEntity<CustomerDto> save(@RequestBody CustomerDto customerDto) {
    customerService.create(customerDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerDto> update(@PathVariable("id") Long id,
                                            @RequestBody CustomerDto customerDto) {
    customerService.update(id, customerDto);
    return ResponseEntity.status(HttpStatus.OK).body(customerDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Customer> delete(@PathVariable("id") Long id) {
    customerService.delete(id);
    return ResponseEntity.noContent().build();
  }
}