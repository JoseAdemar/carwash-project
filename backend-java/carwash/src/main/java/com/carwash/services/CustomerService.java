package com.carwash.services;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDto> findAllCustomer() {
        try {
            List<Customer> customers = customerRepository.findAll();
            if (customers.isEmpty()) {
                throw new ResourceNotFoundException(new ArrayList<>() + "Lista de usuários está vázia");
            }
            return mapToCustomerDto(customers);
        } catch (InternalError e) {
            throw new InternalError(String.format("Erro interno ao tentar buscar todos os usuários"));
        }
    }

    private List<CustomerDto> mapToCustomerDto(List<Customer> customers) {
        return customers.stream().map(customer -> {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        }).collect(Collectors.toList());
    }

    public CustomerDto getCustomerDtoById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new
                ResourceNotFoundException(String.format("Id " + id + " Não encontrado")));
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    public void saveCustomer(CustomerDto customerDto) {
        try {
            Customer customer = new Customer();
            BeanUtils.copyProperties(customerDto, customer);
            Customer customerToSave = customerRepository.save(customer);
        } catch (ResourceStorageException e) {
            throw new ResourceNotFoundException(String.format("Erro interno ao tentar cadastrar usuário"));
        }
    }

    public void updateCustomer(Long id, CustomerDto customerDto) {
        try {
            Optional<Customer> currentCustomer = customerRepository.findById(id);
            if (currentCustomer.isEmpty()) {
                throw new ResourceNotFoundException(String.format("Id %d não encontrado para atualização do usuário", id));
            }
            BeanUtils.copyProperties(customerDto, currentCustomer.get(), "id");
            customerRepository.save(currentCustomer.get());

        } catch (ResourceStorageException e) {
            throw new ResourceStorageException("Erro interno ao tentar atualizar usuário");
        }
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Id " + id + " não encontrado"));
        customerRepository.deleteById(customer.getId());
    }
}
