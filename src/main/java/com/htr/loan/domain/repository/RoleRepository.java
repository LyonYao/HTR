package com.htr.loan.domain.repository;

import com.htr.loan.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleNameAndActiveTrue(String roleName);

    Role save(Role role);
}