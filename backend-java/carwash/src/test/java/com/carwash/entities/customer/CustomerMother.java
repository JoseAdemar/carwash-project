package com.carwash.entities.customer;

import com.carwash.entities.Customer;

import java.util.ArrayList;

@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
public class CustomerMother {
  public static Customer createCustomer1() {
    Customer customer = new Customer();
    customer.setId(1L);
    customer.setName("João");
    customer.setEmail("joao@email.com");
    customer.setPhoneNumber("6185479658");
    return customer;
  }

  public static Customer createCustomer2() {
    Customer customer = new Customer();
    customer.setId(2L);
    customer.setName("Maria");
    customer.setEmail("maria@email.com");
    customer.setPhoneNumber("6195479658");
    return customer;
  }

/*  public static String resourceNotFoundException() {
    return "404 NOT_FOUND",new ArrayList<>()+"Nenhum cliente encontrado na busca";
  }*/
}
