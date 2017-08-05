package com.htr.loan.service;


import com.htr.loan.domain.Role;

public interface RoleService {
    Role findRoleByRoleName(String userAccount);

    Role saveRole(Role role);
}
