package com.htr.loan.web;

import com.htr.loan.domain.Vehicle;
import com.htr.loan.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Vehicle> findAll(){
        return vehicleService.findAll();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Vehicle save(Vehicle vehicle){
        return vehicleService.save(vehicle);
    }

}
