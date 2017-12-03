package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.domain.Role;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.RoleRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;


    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleNameAndActiveTrue(roleName);
    }

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findAllByActiveTrue() {
        return roleRepository.findAllByActiveTrue();
    }

    @Override
    public Role saveRole(Role role) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_ROLE, role.getRoleName());
            if (StringUtils.isNotBlank(role.getUuid())) {
                log.setOperaType(Constants.OPERATYPE_UPDATE);
            } else {
                log.setOperaType(Constants.OPERATYPE_ADD);
            }
            role = roleRepository.save(role);
            log.setRecordId(role.getUuid());
            systemLogRepository.save(log);
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update role " + role.getRoleName() + " fail!");
        }
        return null;
    }

    @Override
    public boolean activeOrStopRoles(List<Role> roles, boolean flag) {
        try {
            SystemLog log;
            for (Role role : roles) {
                if(flag)
                    log = new SystemLog(Constants.MODULE_ROLE, role.getRoleName(), role.getUuid(), Constants.OPERATYPE_ACTIVE);
                else log = new SystemLog(Constants.MODULE_ROLE, role.getRoleName(), role.getUuid(), Constants.OPERATYPE_STOP);
                role.setActive(flag);
                roleRepository.save(role);
                systemLogRepository.save(log);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("active Role fail!");
        }
        return false;
    }
}
