package com.carwash.entities.customer;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.repositories.CustomerRepository;
import com.carwash.services.CustomerService;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CustomerTest {

    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Given_a_List_When_calld_method_findAll_Then_return_a_list_of_customer() {
        List<Customer> mockCustomers = Arrays.asList(CustomerMother.createCustomer1(), CustomerMother.createCustomer2());
        when(customerRepository.findAll()).thenReturn(mockCustomers);
        List<CustomerDto> customerDtos = customerService.findAll();
        assertEquals(2, customerDtos.size());
    }

    @Test
    void Given_an_empty_list_When_call_method_findAll_Then_throw_an_exception() {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());
        ResourceNotFoundException result = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findAll();
        });
        assertEquals(CustomerMother.resourceNotFoundException(), result.getMessage());
    }

    @Test
    void Given_an_id_or_name_or_email_When_call_method_findByCriteria_Then_return_a_customer() {
        Given_an_id_When_call_method_findById_Then_return_a_customer();
        Given_an_email_When_call_method_findByEmail_Then_return_a_customer();
        Given_a_name_When_call_method_findByName_Then_return_a_customer();

        Optional<CustomerDto> customerDto = customerService.findByCriteria(null, null, "joao@email.com");
        assertFalse(customerDto.isEmpty());

        Optional<CustomerDto> result1 = customerService.findByCriteria(1L, null, null);
        assertFalse(customerDto.isEmpty());

        Optional<CustomerDto> result2 = customerService.findByCriteria(null, null, "joao@email.com");
        assertFalse(customerDto.isEmpty());

        Optional<CustomerDto> result3 = customerService.findByCriteria(null, "João", null);
        assertFalse(customerDto.isEmpty());
    }

    @Test
    void Given_an_id_null_name_null_email_null_When_call_method_findByCriteria_Then_return_an_exception() {
        Given_an_id_When_call_method_findById_Then_return_a_customer();
        Given_an_email_When_call_method_findByEmail_Then_return_a_customer();
        Given_a_name_When_call_method_findByName_Then_return_a_customer();

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findByCriteria(null, null, null);
        });

        assertEquals(resourceNotFoundException.getMessage(), "Nenhum dado encontrado na pesquisa");
    }


    @Test
    void Given_an_id_When_call_method_findById_Then_return_a_customer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(CustomerMother.createCustomer1()));
        Optional<Customer> result = customerRepository.findById(1L);
        assertEquals(Optional.of(CustomerMother.createCustomer1()), result);
    }

    @Test
    void Given_an_email_When_call_method_findByEmail_Then_return_a_customer() {
        when(customerRepository.findByEmail(CustomerMother.createCustomer1().getEmail()))
                .thenReturn(Optional.of(CustomerMother.createCustomer1()));
        Optional<Customer> result = customerRepository.findByEmail(CustomerMother.createCustomer1().getEmail());
        assertEquals(Optional.of(CustomerMother.createCustomer1()), result);
    }

    @Test
    void Given_a_name_When_call_method_findByName_Then_return_a_customer() {
        List<Customer> customers = new ArrayList<>();
        customers.add(CustomerMother.createCustomer1());

        when(customerRepository.findByName(CustomerMother.createCustomer1().getName(), PageRequest.of(0, 1)))
                .thenReturn(customers);

        List<Customer> resultList = customerRepository.findByName(CustomerMother.createCustomer1().getName(), PageRequest.of(0, 1));
        assertEquals(customers, resultList);
    }
}