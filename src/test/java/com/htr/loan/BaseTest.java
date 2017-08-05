package com.htr.loan;

import com.htr.loan.domain.Role;
import com.htr.loan.domain.User;
import com.htr.loan.service.RoleService;
import com.htr.loan.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;

public class BaseTest extends LoanApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setActive(true);
        user.setUserAccount("xinlin");
        user.setPassword("xinlin");
        Role userRole = roleService.findRoleByRoleName("超级管理员");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userService.saveUser(user);
    }

    @Test
    public void testSaveRole() {
        Role role = new Role();
        role.setActive(true);
        role.setDescription("超级管理员拥有至高无上的权利!!");
        role.setRoleName("超级管理员");
        role = roleService.saveRole(role);
        System.out.println(role.getUuid());
    }
}
