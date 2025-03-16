package com.carwash.repositories;

import com.carwash.controllers.dtos.ServiceOrderVehicleDTO;
import com.carwash.entities.ServiceOrder;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

  @Query(value = "SELECT " +
          "   service_order_vehicle.service_order_id AS orderId, " +
          "   service_order.price AS price, " +
          "   service_order.wash_status AS status, " +
          "   service_order.wash_type AS type, " +
          "   service_order.date AS date_time, " +
          "   vehicle.car_model AS model, " +
          "   vehicle.license AS license, " +
          "   customer.name AS name " +
          "FROM " +
          "   service_order_vehicle " +
          "INNER JOIN " +
          "   service_order ON service_order_vehicle.service_order_id = service_order.id " +
          "INNER JOIN " +
          "   vehicle ON service_order_vehicle.vehicles_id = vehicle.id " +
          "INNER JOIN " +
          "   customer ON vehicle.customer_id = customer.id " +
          "WHERE service_order.wash_status != 'FINALIZADO'", nativeQuery = true)
  List<Tuple> findServiceOrdersWithDetails();
}
