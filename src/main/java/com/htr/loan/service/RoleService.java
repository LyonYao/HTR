package com.htr.loan.service;


import com.htr.loan.domain.Role;

import java.util.List;

public interface RoleService {

    Role findRoleByRoleName(String userAccount);

    Role saveRole(Role role);

    List<Role> findAllRole();

    List<Role> findAllByActiveTrue();

    boolean activeOrStopRoles(List<Role> roles, boolean flag);
}
