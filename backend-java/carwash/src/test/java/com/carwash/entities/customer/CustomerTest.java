package com.carwash.entities.customer;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.CustomerRepository;
import com.carwash.services.CustomerService;
import org.glassfish.jaxb.core.v2.TODO;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
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
    List<Customer> expectedCustomers = Arrays
            .asList(CustomerMother.createCustomer1(), CustomerMother.createCustomer2());

    when(customerRepository.findAll()).thenReturn(expectedCustomers);
    List<CustomerDto> actualCustomer = customerService.findAll();

    assertEquals(expectedCustomers.get(0).getName(), actualCustomer.get(0).getName());
    assertEquals(expectedCustomers.get(1).getName(), actualCustomer.get(1).getName());
    assertEquals(expectedCustomers.size(), actualCustomer.size());
    verify(customerRepository).findAll();
  }

  @Test
  void given_an_empty_list_When_call_method_findAll_Then_throw_an_exception() {
    String expectedExceptionMessage = "404 NOT_FOUND \"[]Nenhum cliente encontrado na busca\"";

    when(customerRepository.findAll()).thenReturn(new ArrayList<>());
    ResponseStatusException actualResult = assertThrows(ResponseStatusException.class, () -> {
      customerService.findAll();
    });

    assertEquals(expectedExceptionMessage, actualResult.getMessage());
  }

  @Test
  void given_an_id_or_name_or_email_When_call_method_findByCriteria_Then_return_a_customer() {

    Customer customer = CustomerMother.createCustomer1();
    List<Customer> customers = new ArrayList<>();
    customers.add(customer);
    String expectedEmail = customer.getEmail();
    long expectedId = customer.getId();
    String expectedName = customer.getName();

    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
    when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
    when(customerRepository.findByName(customer.getName())).thenReturn(customers);

    Optional<CustomerDto> actualResult1 = customerService
            .findByCriteria(null, null, customer.getEmail());
    assertEquals(expectedEmail, actualResult1.get().getEmail());

    Optional<CustomerDto> actualResult2 = customerService
            .findByCriteria(customer.getId(), null, null);
    assertEquals(expectedId, actualResult2.get().getId());

    Optional<CustomerDto> actualResult3 = customerService
            .findByCriteria(null, customer.getName(), null);
    assertEquals(expectedName, actualResult3.get().getName());
  }

  @Test
  void given_an_id_name_email_null_When_call_method_findByCriteria_Then_return_an_exception() {

    String expectedExceptionMessage = "404 NOT_FOUND \"Nenhum dado encontrado na pesquisa\"";

    ResponseStatusException actualException =
            assertThrows(ResponseStatusException.class, () -> {
              customerService.findByCriteria(null, null, null);
            });

    assertEquals(expectedExceptionMessage, actualException.getMessage());
  }


  @Test
  void given_an_id_When_call_method_findById_Then_return_a_customer() {
    Customer customer = CustomerMother.createCustomer1();
    String expectedCustomerName = customer.getName();

    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
    Optional<CustomerDto> actualResult = Optional.of(customerService.getById(customer.getId()));

    assertEquals(expectedCustomerName, actualResult.get().getName());
    verify(customerRepository).findById(customer.getId());
  }

  @Test
  void given_an_id_null_When_call_method_findById_Then_return_an_exception() {
    Long id = null;
    String expectedException = String.format("Id " + id + " Não é válido");

    NullPointerException actualException = assertThrows(NullPointerException.class, () -> {
      customerService.getById(id);
    });

    assertEquals(expectedException, actualException.getMessage());
  }

  @Test
  void given_a_nonexistent_id_When_call_method_findById_Then_return_an_exception() {
    Long id = 100L;
    String expectedException = "404 NOT_FOUND \"Id " + id + " não encontrado\"";
    String exceptionMessage = "Id " + id + " não encontrado";

    when(customerRepository.findById(id))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                    exceptionMessage));
    ResponseStatusException actualException =
            assertThrows(ResponseStatusException.class, () -> {
              customerService.getById(id);
            });

    assertEquals(expectedException, actualException.getMessage());
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
    //TODO
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
    //TODO
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
    //TODO
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
    //TODO
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
    //TODO
  }
}