package com.htr.loan.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleNameAndActiveTrue(String roleName);

    Role save(Role role);
}