package com.htr.loan.service;


import com.htr.loan.domain.User;

import java.util.List;

public interface UserService {
    User findUserByUserAccount(String userAccount);

    User saveUser(User user, boolean isChangePwd);

    List<User> findAllUser();

    List<User> findAllByActiveTrue();

    boolean activeOrStopUsers(List<User> users, boolean flag);
}
