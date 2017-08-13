package com.htr.loan.service.impl;

import com.htr.loan.domain.Vehicle;
import com.htr.loan.domain.repository.VehicleRepository;
import com.htr.loan.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleServiceImpl.class);

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAllByActiveTrue();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        try {
            vehicle.setActive(true);
            vehicle = vehicleRepository.save(vehicle);
            return vehicle;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update vehicle " + vehicle.getBrand() + " fail!");
        }
        return null;
    }
}
