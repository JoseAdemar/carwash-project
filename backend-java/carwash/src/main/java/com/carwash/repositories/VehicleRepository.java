package com.carwash.repositories;

import com.carwash.entities.Customer;
import com.carwash.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
  @Query("SELECT v FROM Vehicle v WHERE v.licensePlate = :licensePlate")
  Optional<Vehicle> findByLicensePlate(@Param("licensePlate") String license);
}
