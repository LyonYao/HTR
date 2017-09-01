package com.htr.loan.web;

import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.Role;
import com.htr.loan.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/{active}", method = RequestMethod.GET)
    public List<Role> findAllRole(@PathVariable int active){
        if (active == 0){
            return roleService.findAllRole();
        } else return roleService.findAllByActiveTrue();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Role saveRole(@RequestBody Role role){
        return roleService.saveRole(role);
    }

    @RequestMapping(value = "stop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> stopRole(@RequestBody List<Role> roles){
        boolean isDeleted = roleService.activeOrStopRoles(roles, false);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }

    @RequestMapping(value = "active", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> activeRole(@RequestBody List<Role> roles){
        boolean isDeleted = roleService.activeOrStopRoles(roles, true);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }
}
