package com.htr.loan.service.impl;

import com.htr.loan.domain.Role;
import com.htr.loan.domain.repository.RoleRepository;
import com.htr.loan.service.RoleService;
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
            role.setActive(true);
            role = roleRepository.save(role);
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update Role" + role.getRoleName() + " fail!");
        }
        return null;
    }
}
