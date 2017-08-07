package com.htr.loan.service;

import com.htr.loan.domain.User;
import com.htr.loan.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByUserAccount(String userAccount) {
        return userRepository.findByUserAccountAndActiveTrue(userAccount);
    }

    @Override
    public User saveUser(User user) {
        try {
            user.setActive(true);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user = userRepository.save(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
