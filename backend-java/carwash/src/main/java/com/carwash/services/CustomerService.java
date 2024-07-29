package com.carwash.services;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
@Service
public class CustomerService {
  @Autowired
  private CustomerRepository customerRepository;

  public List<CustomerDto> findAll() {
    try {
      List<Customer> customers = customerRepository.findAll();
      if (customers.isEmpty()) {
        throw new ResourceNotFoundException(
                new ArrayList<>() + "Nenhum cliente encontrado na busca");
      }
      List<CustomerDto> customerDtos = mapToDto(customers);
      return customerDtos;
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  private List<CustomerDto> mapToDto(List<Customer> customers) {
    return customers.stream().map(customer -> {
      CustomerDto customerDto = new CustomerDto();
      BeanUtils.copyProperties(customer, customerDto);
      return customerDto;
    }).collect(Collectors.toList());
  }

  public Optional<CustomerDto> findByCriteria(Long id, String name, String email) {
    try {
      if (id != null) {
        return Optional.of(getById(id));
      }
      if (email != null && !email.isBlank()) {
        email = email.strip();
        return findByEmail(email);
      }
      if (name != null && !name.isBlank()) {
        name = name.strip();
        return findByName(name);
      }
      throw new ResourceNotFoundException(String.format("Nenhum dado encontrado na pesquisa"));
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  public CustomerDto getById(Long id) {
    Objects.requireNonNull(id,String.format("Id " + id + " Não é válido"));
    try {
      Customer customer = customerRepository.findById(id).orElseThrow(() ->
              new ResourceNotFoundException(String.format("Id %d não encontrado", id)));
      CustomerDto customerDto = new CustomerDto();
      BeanUtils.copyProperties(customer, customerDto);
      return customerDto;

    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  private Optional<CustomerDto> findByName(String name) {
    if (name != null & !name.strip().isBlank()) {
      final long maxOneCustomer = 1;
      Customer customer = customerRepository.findByName(name)
              .stream()
              .limit(maxOneCustomer).findFirst()
              .orElseThrow(
                      () -> new ResourceNotFoundException("Nenhum dado encontrado na pesquisa"));
      CustomerDto customerDto = new CustomerDto();
      BeanUtils.copyProperties(customer, customerDto);
      return Optional.of(customerDto);
    } else {
      throw new IllegalArgumentException(String.format("O valor não pode ser null ou vazio"));
    }
  }

  private Optional<CustomerDto> findByEmail(String email) {
    if (email != null && !email.strip().isBlank()) {
      Customer customer = customerRepository.findByEmail(email)
              .stream().findFirst().orElseThrow(() ->
                      new ResourceNotFoundException(
                              String.format("Busca referente ao email %s não encontrada", email)));
      CustomerDto customerDto = new CustomerDto();
      BeanUtils.copyProperties(customer, customerDto);
      return Optional.of(customerDto);
    } else {
      throw new IllegalArgumentException(String.format("O valor não pode ser null ou vazio"));
    }
  }

  @Transactional
  public void create(CustomerDto customerDto) {
    try {
      Customer customer = new Customer();
      BeanUtils.copyProperties(customerDto, customer);
      customerRepository.save(customer);
    } catch (ResourceStorageException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
              new ResourceStorageException(
                      String.format("Erro interno ao tentar cadastrar usuário")).getMessage());
    }
  }

  @Transactional
  public void update(Long id, CustomerDto customerDto) {
    try {
      Customer currentCustomer = customerRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException(
                      String.format("Id %d não encontrado para atualização do usuário", id)));
      BeanUtils.copyProperties(customerDto, currentCustomer, "id");
      customerRepository.save(currentCustomer);
    } catch (ResourceStorageException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
              new ResourceStorageException(
                      "Erro interno ao tentar atualizar usuário").getMessage());
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @Transactional
  public void delete(Long id) {
    try {
      Customer customer = customerRepository.findById(id).orElseThrow(() ->
              new ResourceNotFoundException("Id " + id + " não encontrado"));
      customerRepository.deleteById(customer.getId());
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }
}
