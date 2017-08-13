package com.htr.loan.service;

import com.htr.loan.domain.Vehicle;

import java.util.List;

public interface VehicleService {

    List<Vehicle> findAll();

    Vehicle save(Vehicle vehicle);
}
