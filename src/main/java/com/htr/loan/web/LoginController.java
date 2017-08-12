package com.htr.loan.web;

import com.htr.loan.domain.User;
import com.htr.loan.service.UserService;
import com.htr.loan.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserService userService;

    // Match everything without a suffix (so not a static resource)
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Forward to home page so that route is preserved.
        return "forward:/";
    }

    @RequestMapping("/loginUser")
    @ResponseBody
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/sessionUser")
    @ResponseBody
    public User getCurrentUser(HttpSession session) {
        User user = (User)session.getAttribute("loginUser");
        return user;
    }
}
