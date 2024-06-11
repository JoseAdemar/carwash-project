package com.carwash.entities.customer;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.CustomerRepository;
import com.carwash.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
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
  void given_a_List_When_calld_method_findAll_Then_return_a_list_of_customer() {
    List<Customer> mockCustomers = Arrays
            .asList(CustomerMother.createCustomer1(), CustomerMother.createCustomer2());
    when(customerRepository.findAll()).thenReturn(mockCustomers);
    List<CustomerDto> customerDtos = customerService.findAll();
    assertEquals(2, customerDtos.size());
  }

  @Test
  void given_an_empty_list_When_call_method_findAll_Then_throw_an_exception() {
    when(customerRepository.findAll()).thenReturn(new ArrayList<>());
    ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> {
      customerService.findAll();
    });
    assertEquals("404 NOT_FOUND \"[]Nenhum cliente encontrado na busca\"", result.getMessage());
  }

  @Test
  void given_an_id_or_name_or_email_When_call_method_findByCriteria_Then_return_a_customer() {
    given_an_id_When_call_method_findById_Then_return_a_customer();
    when(customerRepository.findByEmail("joao@email.com")).thenReturn(
            Optional.of(CustomerMother.createCustomer1()));
    List<Customer> customers = new ArrayList<>();
    customers.add(CustomerMother.createCustomer1());

    when(customerRepository.findByName("João")).thenReturn(customers);

    Optional<CustomerDto> result1 = customerService.findByCriteria(null, null, "joao@email.com");
    assertEquals("joao@email.com", result1.get().getEmail());

    Optional<CustomerDto> result2 = customerService.findByCriteria(1L, null, null);
    assertEquals(1L, result2.get().getId());

    Optional<CustomerDto> result3 = customerService.findByCriteria(null, "João", null);
    assertEquals("João", result3.get().getName());
  }

  @Test
  void given_an_id_name_email_null_When_call_method_findByCriteria_Then_return_an_exception() {
    given_an_id_When_call_method_findById_Then_return_a_customer();

    ResponseStatusException exception =
            assertThrows(ResponseStatusException.class, () -> {
              customerService.findByCriteria(null, null, null);
            });
    assertEquals(exception.getMessage(),
            "404 NOT_FOUND \"Nenhum dado encontrado na pesquisa\"");
  }


  @Test
  void given_an_id_When_call_method_findById_Then_return_a_customer() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(CustomerMother.createCustomer1()));
    Optional<Customer> result = customerRepository.findById(1L);
    assertEquals(Optional.of(CustomerMother.createCustomer1()), result);
  }

  @Test
  void given_an_id_null_When_call_method_findById_Then_return_an_exception() {
    Long id = null;
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      customerService.getById(id);
    });
    assertEquals("400 BAD_REQUEST \"Campo id = " + id + " não é permitido\"",
            exception.getMessage());
  }

  @Test
  void given_a_nonexistent_id_When_call_method_findById_Then_return_an_exception() {
    Long id = 1000L;
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      customerService.getById(id);
    });
    assertEquals("404 NOT_FOUND \"Id " + id + " Não encontrado\"", exception.getMessage());
  }

  @Test
  void given_a_null_name_When_call_method_findByName_and_not_found_Then_return_an_exception() {
    String name = null;
    when(customerRepository.findByName(name)).thenThrow(
            new IllegalArgumentException(String.format("O valor não pode ser null ou vazio")));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      customerRepository.findByName(name);
    });

    assertEquals(String.format("O valor não pode ser null ou vazio"), exception.getMessage());
  }

  @Test
  void given_a_customer_When_callded_method_createCustomer_Then_persist_customer() {
    Customer customer = CustomerMother.createCustomer1();
    CustomerDto customerDto = new CustomerDto();
    BeanUtils.copyProperties(customer, customerDto);

    when(customerRepository.save(any(Customer.class))).thenReturn(customer);

    customerService.create(customerDto);

    ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
    verify(customerRepository).save((customerCaptor.capture()));

    Customer capturedCustomer = customerCaptor.getValue();
    assertEquals(customer.getId(), capturedCustomer.getId());
    assertEquals(customer.getName(), capturedCustomer.getName());
    assertEquals(customer.getEmail(), capturedCustomer.getEmail());
  }

  @Test
  void given_a_customer_When_callded_method_createCustomer_Then_throw_exception_if_persist_fails() {
    Customer customer = CustomerMother.createCustomer1();
    CustomerDto customerDto = new CustomerDto();
    BeanUtils.copyProperties(customer, customerDto);

    when(customerRepository.save(any(Customer.class)))
            .thenThrow(new ResponseStatusException(HttpStatus
                    .INTERNAL_SERVER_ERROR, String
                    .format("500 INTERNAL_SERVER_ERROR "
                            + "\"Erro interno ao tentar cadastrar usuário\"")));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      customerService.create(customerDto);
    });
    assertEquals("500 INTERNAL_SERVER_ERROR \"Erro interno ao tentar cadastrar usuário\"",
            exception.getReason());
    verify(customerRepository).save(any(Customer.class));
  }

  @Test
  void given_an_id_and_a_customer_When_called_method_updateCustomer_Then_customer_is_updated() {
    Customer customer = CustomerMother.createCustomer1();
    customer.setId(1L);
    customer.setName("Usuário Atualizado");
    customer.setEmail("usuario@atualizado.com");
    customer.setPhoneNumber("9999-9999");

    CustomerDto customerDto = new CustomerDto();
    BeanUtils.copyProperties(customer, customerDto);

    when(customerRepository.findById(1L)).thenReturn(Optional.of(CustomerMother.createCustomer1()));


    customerService.update(customerDto.getId(), customerDto);

    verify(customerRepository, times(1)).save(customer);

    assertEquals(customerDto.getId(), customer.getId());
    assertEquals(customerDto.getName(), customer.getName());
    assertEquals(customerDto.getEmail(), customer.getEmail());
    assertEquals(customerDto.getPhoneNumber(), customer.getPhoneNumber());
  }

  @Test
  void given_and_id_When_callded_method_deleteCustomer_Then_customer_is_deleted() {
    Long customerId = 1L;
    Customer customer = CustomerMother.createCustomer1();
    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

    customerService.delete(customerId);

    verify(customerRepository, times(1)).deleteById(customerId);
    verify(customerRepository, times(1)).findById(customerId);
    verifyNoMoreInteractions(customerRepository);
  }
}