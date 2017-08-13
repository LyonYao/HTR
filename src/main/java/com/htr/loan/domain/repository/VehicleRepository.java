package com.htr.loan.domain.repository;

import com.htr.loan.domain.Role;
import com.htr.loan.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    Vehicle save(Vehicle vehicle);

    List<Vehicle> findAllByActiveTrue();

}