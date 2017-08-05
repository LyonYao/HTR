package com.htr.loan.service;


import com.htr.loan.domain.User;

public interface UserService {
    User findUserByUserAccount(String userAccount);
    User saveUser(User user);
}
