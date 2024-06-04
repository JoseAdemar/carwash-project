package com.carwash.entities.customer;

import com.carwash.controllers.dtos.CustomerDto;
import com.carwash.entities.Customer;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.CustomerRepository;
import com.carwash.services.CustomerService;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.BooleanSupplier;

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
    void Given_an_id_null_When_call_method_findById_Then_return_an_exception() {
        Long id = null;
        ResourceStorageException exception = assertThrows(ResourceStorageException.class, () -> {
            customerService.getCustomerDtoById(id);
        });
        assertEquals("Campo id = " + id + " não é permitido", exception.getMessage());
    }

    @Test
    void Given_a_nonexistent_id_When_call_method_findById_Then_return_an_exception() {
        Long id = 1000L;
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerDtoById(id);
        });
        assertEquals("Id " + id + " Não encontrado", exception.getMessage());
    }

    @Test
    void Given_an_email_When_call_method_findByEmail_Then_return_a_customer() {
        when(customerRepository.findByEmail(CustomerMother.createCustomer1().getEmail()))
                .thenReturn(Optional.of(CustomerMother.createCustomer1()));

        Optional<Customer> result = customerRepository.findByEmail(CustomerMother.createCustomer1().getEmail());
        assertEquals(Optional.of(CustomerMother.createCustomer1()), result);
    }

    @Test
    void Given_a_nonexistent_email_When_call_method_findByEmail_Then_return_an_exception() {
        String email = "teste@email.com";
        when(customerRepository.findByEmail(email)).thenThrow(new ResourceNotFoundException(String.format("Busca referente ao email %s não encontrada", email)));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findByEmail(email);
        });

        assertEquals(String.format("Busca referente ao email %s não encontrada", email), exception.getMessage());
    }

    @Test
    void Given_a_null_email_When_call_method_findByEmail_Then_return_an_exception() {
        String email = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.findByEmail(email);
        });
        assertEquals(String.format("O valor não pode ser null ou vazio"), exception.getMessage());
    }

    @Test
    void Given_a_name_When_call_method_findByName_Then_return_a_customer() {
        List<Customer> customers = new ArrayList<>();
        customers.add(CustomerMother.createCustomer1());

        when(customerRepository.findByName(CustomerMother.createCustomer1().getName()))
                .thenReturn(customers);

        List<Customer> resultList = customerRepository.findByName(CustomerMother.createCustomer1().getName());
        assertEquals(customers, resultList);
    }

    @Test
    void Given_a_name_When_call_method_findByName_and_not_found_Then_return_an_exception() {
        String name = "NonExistentCustomer";
        when(customerRepository.findByName(name)).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findByName(name);
        });

        assertEquals("Nenhum dado encontrado na pesquisa", exception.getMessage());
    }

    @Test
    void Given_a_null_name_When_call_method_findByName_and_not_found_Then_return_an_exception() {
        String name = null;
        when(customerRepository.findByName(name)).thenThrow(new IllegalArgumentException(String.format("O valor não pode ser null ou vazio")));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerRepository.findByName(name);
        });

        assertEquals(String.format("O valor não pode ser null ou vazio"), exception.getMessage());
    }

    @Test
    void Given_a_customer_When_callded_method_createCustomer_Then_persist_customer() {
        Customer customer = CustomerMother.createCustomer1();
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.createCustomer(customerDto);

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save((customerCaptor.capture()));

        Customer capturedCustomer = customerCaptor.getValue();
        assertEquals(customer.getId(), capturedCustomer.getId());
        assertEquals(customer.getName(), capturedCustomer.getName());
        assertEquals(customer.getEmail(), capturedCustomer.getEmail());
    }

    @Test
    void Given_a_customer_When_callded_method_createCustomer_Then_throw_exception_if_persist_fails() {
        Customer customer = CustomerMother.createCustomer1();
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);

        when(customerRepository.save(any(Customer.class))).thenThrow(new ResourceStorageException(String.format("Erro interno ao tentar cadastrar usuário")));

        ResourceStorageException exception = assertThrows(ResourceStorageException.class, () -> {
            customerService.createCustomer(customerDto);
        });

        assertEquals(String.format("Erro interno ao tentar cadastrar usuário"), exception.getMessage());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void Given_an_id_and_a_customer_When_called_method_updateCustomer_Then_customer_is_updated() {
        Customer customer = CustomerMother.createCustomer1();
        customer.setId(1L);
        customer.setName("Usuário Atualizado");
        customer.setEmail("usuario@atualizado.com");
        customer.setPhoneNumber("9999-9999");

        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(CustomerMother.createCustomer1()));

        customerService.updateCustomer(customerDto.getId(), customerDto);

        verify(customerRepository, times(1)).save(customer);

        assertEquals(customerDto.getId(), customer.getId());
        assertEquals(customerDto.getName(), customer.getName());
        assertEquals(customerDto.getEmail(), customer.getEmail());
        assertEquals(customerDto.getPhoneNumber(), customer.getPhoneNumber());
    }

    @Test
    void Given_and_id_When_callded_method_deleteCustomer_Then_customer_is_deleted(){
        Long customerId = 1L;
        Customer customer = CustomerMother.createCustomer1();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
        verify(customerRepository, times(1)).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
    }
}