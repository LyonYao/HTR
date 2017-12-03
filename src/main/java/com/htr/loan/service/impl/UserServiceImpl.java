package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.domain.Role;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.User;
import com.htr.loan.domain.repository.RoleRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.domain.repository.UserRepository;
import com.htr.loan.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User findUserByUserAccount(String userAccount) {
        return userRepository.findByUserAccountAndActiveTrue(userAccount);
    }

    @Override
    public User saveUser(User user, boolean isChangePwd) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_USER, user.getUserAccount());
            if (isChangePwd){
                log.setOperaType(Constants.OPERATYPE_CHANGEPWD);
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            } else if (StringUtils.isNotBlank(user.getUuid())) {
                log.setOperaType(Constants.OPERATYPE_UPDATE);
                if(user.getPassword().length() <= Constants.DEFAULT_PASSWORD_MAXLENGTH) {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                }
            } else {
                log.setOperaType(Constants.OPERATYPE_ADD);
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            user = userRepository.save(user);
            log.setRecordId(user.getUuid());
            systemLogRepository.save(log);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update user " + user.getUserAccount() + " fail!");
        }
        return null;
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllByActiveTrue() {
        return userRepository.findAllByActiveTrue();
    }

    @Override
    public List<User> findAllInstaller() {
        return userRepository.findAllByActiveTrueAndRoles_RoleName(Constants.INIT_ROLE_INSTALLER);
    }

    @Override
    public boolean activeOrStopUsers(List<User> users, boolean flag) {
        try {
            SystemLog log;
            for (User user : users) {
                if(flag)
                    log = new SystemLog(Constants.MODULE_USER, user.getUserAccount(), user.getUuid(), Constants.OPERATYPE_ACTIVE);
                else log = new SystemLog(Constants.MODULE_USER, user.getUserAccount(), user.getUuid(), Constants.OPERATYPE_STOP);
                user.setActive(flag);
                userRepository.save(user);
                systemLogRepository.save(log);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("active User fail!");
        }
        return false;
    }

}
