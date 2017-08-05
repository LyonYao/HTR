package com.htr.loan.service;

import com.htr.loan.domain.Role;
import com.htr.loan.domain.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleNameAndActiveTrue(roleName);
    }

    @Override
    public Role saveRole(Role role) {
        try {
            role = roleRepository.save(role);
            return role;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
