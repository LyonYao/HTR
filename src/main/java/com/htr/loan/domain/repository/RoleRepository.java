package com.htr.loan.domain.repository;

import com.htr.loan.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    Role findByRoleNameAndActiveTrue(String roleName);

    Role findByRoleName(String roleName);

    List<Role> findAllByActiveTrue();
}