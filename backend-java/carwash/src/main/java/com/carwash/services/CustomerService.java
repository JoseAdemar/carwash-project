package com.carwash.services;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDto> findAll() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new ResourceNotFoundException(new ArrayList<>() + "Nenhum cliente encontrado na busca");
        }
        List<CustomerDto> customerDtos = mapToCustomerDto(customers);
        return customerDtos;
    }

    private List<CustomerDto> mapToCustomerDto(List<Customer> customers) {
        return customers.stream().map(customer -> {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        }).collect(Collectors.toList());
    }

    public Optional<CustomerDto> findByCriteria(Long id, String name, String email) {
        if (id != null) {
            return Optional.of(getCustomerDtoById(id));
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
    }

    public Optional<CustomerDto> findByName(String name) {
        if (name != null & !name.strip().isBlank()) {
            final long maxOneCustomer = 1;
            Customer customer = customerRepository.findByName(name).stream().limit(maxOneCustomer).findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum dado encontrado na pesquisa"));

            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.of(customerDto);
        } else {
            throw new IllegalArgumentException(String.format("O valor não pode ser null ou vazio"));
        }
    }

    public Optional<CustomerDto> findByEmail(String email) {
        if (email != null && !email.strip().isBlank()) {
            Customer customer = customerRepository.findByEmail(email)
                    .stream().findFirst().orElseThrow(() ->
                            new ResourceNotFoundException(String.format("Busca referente ao email %s não encontrada", email)));
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.of(customerDto);
        } else {
            throw new IllegalArgumentException(String.format("O valor não pode ser null ou vazio"));
        }
    }

    public CustomerDto getCustomerDtoById(Long id) {
        if (id != null) {
            Customer customer = customerRepository.findById(id).orElseThrow(() -> new
                    ResourceNotFoundException(String.format("Id " + id + " Não encontrado")));
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        } else {
            throw new ResourceStorageException("Campo id = " + id + " não é permitido");
        }
    }

    @Transactional
    public void createCustomer(CustomerDto customerDto) {
        try {
            Customer customer = new Customer();
            BeanUtils.copyProperties(customerDto, customer);
            customerRepository.save(customer);
        } catch (ResourceStorageException e) {
            throw new ResourceStorageException(String.format("Erro interno ao tentar cadastrar usuário"));
        }
    }

    @Transactional
    public void updateCustomer(Long id, CustomerDto customerDto) {
        try {
            Customer currentCustomer = customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Id %d não encontrado para atualização do usuário", id)));
            BeanUtils.copyProperties(customerDto, currentCustomer, "id");
            customerRepository.save(currentCustomer);

        } catch (ResourceStorageException e) {
            throw new ResourceStorageException("Erro interno ao tentar atualizar usuário");
        }
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Id " + id + " não encontrado"));
        customerRepository.deleteById(customer.getId());
    }
}
