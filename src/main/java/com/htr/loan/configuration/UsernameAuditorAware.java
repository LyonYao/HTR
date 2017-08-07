package com.htr.loan.configuration;
import com.htr.loan.domain.User;
import com.htr.loan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

@Configuration
public class UsernameAuditorAware implements AuditorAware<User> {

    private User user;
    @Autowired
    private UserService userService;

    @Override
    public User getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String userAccount = ((Principal) authentication.getPrincipal()).getName();

        if (user == null) {
            user = userService.findUserByUserAccount(userAccount);
        }
        return user;
    }
}