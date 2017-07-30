package com.htr.loan.service;


import com.htr.loan.domain.User;

public interface UserService {
    public User findUserByUserName(String userName);
    public void saveUser(User user);
}
