package com.carwash.repositories;

import com.carwash.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);

  @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name%")
    List<Customer> findByName(@Param("name") String name);
}
