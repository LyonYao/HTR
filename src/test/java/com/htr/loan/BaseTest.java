package com.htr.loan;

import com.htr.loan.domain.Resource;
import com.htr.loan.domain.Role;
import com.htr.loan.domain.User;
import com.htr.loan.service.ResourceService;
import com.htr.loan.service.RoleService;
import com.htr.loan.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;

public class BaseTest extends LoanApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

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

    @Test
    public void testSaveResource() {

        Resource resource = new Resource();
        resource.setResourceName("系统管理");
        resource.setActive(true);
        resource = resourceService.saveResource(resource);

        Resource subResource1 = new Resource();
        subResource1.setResourceName("菜单管理");
        subResource1.setResPath("menuList");
        subResource1.setParentRes(resource);
        subResource1.setActive(true);
        subResource1 = resourceService.saveResource(subResource1);

        Resource subResource2 = new Resource();
        subResource2.setResourceName("用户管理");
        subResource2.setResPath("userList");
        subResource2.setParentRes(resource);
        subResource2.setActive(true);
        subResource2 = resourceService.saveResource(subResource2);

        Resource subResource3 = new Resource();
        subResource3.setResourceName("角色管理");
        subResource3.setResPath("roleList");
        subResource3.setParentRes(resource);
        subResource3.setActive(true);
        subResource3 = resourceService.saveResource(subResource3);

        resource.setChildrenRes(Arrays.asList(subResource1,subResource2,subResource3));


        resource = resourceService.saveResource(resource);
        System.out.println(resource.getUuid());
    }


}
