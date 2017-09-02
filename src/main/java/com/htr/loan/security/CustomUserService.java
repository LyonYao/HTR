package com.htr.loan.security;

import com.htr.loan.Utils.Constants;
import com.htr.loan.domain.Role;
import com.htr.loan.domain.User;
import com.htr.loan.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomUserService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userService.findUserByUserAccount(userName);
        if(user == null){
            LOG.warn("userAccount does not exist!!");
            throw new UsernameNotFoundException("userAccount does not exist!!");
        } else {
            ServletRequestAttributes attr = (ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes();
            HttpSession session= attr.getRequest().getSession(true);
            session.setAttribute(Constants.SESSION_USER_KEY, user);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            //用于添加用户的权限。只要把用户权限添加到authorities 就万事大吉。
            for(Role role:user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            }

            return new org.springframework.security.core.userdetails.User(user.getUserAccount(),
                    user.getPassword(), authorities);
        }
    }
}
