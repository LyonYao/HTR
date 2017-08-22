package com.htr.loan.service;

import com.htr.loan.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface VehicleService {

    Vehicle saveVehicle(Vehicle vehicle);

    Page<Vehicle> findAll(Map<String,Object> filterParams, Pageable pageable);

    boolean removeVehicles(List<Vehicle> vehicleList);
}
