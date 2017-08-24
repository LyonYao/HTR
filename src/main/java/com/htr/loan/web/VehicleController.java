package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.Vehicle;
import com.htr.loan.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @RequestMapping(value = "/{currentPage}/{pageSize}", method = RequestMethod.GET)
    public Page<Vehicle> findAll(@PathVariable int currentPage,
                                @PathVariable int pageSize,
                                @RequestParam(defaultValue = "{}") String jsonFilter){
        Map<String, Object> filterParams = WebUtil.getParametersStartingWith(jsonFilter, Constants.SEARCH_PREFIX);
        filterParams.put("EQ_active", Constants.RECORD_EXIST);

        return vehicleService.findAll(filterParams, WebUtil.buildPageRequest(currentPage, pageSize));
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Vehicle saveVehicle(@RequestBody Vehicle Vehicle){
        return vehicleService.saveVehicle(Vehicle);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> removeVehicle(@RequestBody List<Vehicle> Vehicles){
        boolean isDeleted = vehicleService.removeVehicles(Vehicles);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }

    @RequestMapping(value = "detain", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> detainVehicle(@RequestBody List<Vehicle> Vehicles){
        boolean isDeleted = vehicleService.detainOrNotVehicles(Vehicles, true);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }

    @RequestMapping(value = "unDetain", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> unDetainVehicle(@RequestBody List<Vehicle> Vehicles){
        boolean isDeleted = vehicleService.detainOrNotVehicles(Vehicles, false);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }

}
