package com.htr.loan.web;

import com.htr.loan.domain.User;
import com.htr.loan.service.UserService;
import com.htr.loan.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public User user(Principal user, HttpSession session) {

        User loginUser = userService.findUserByUserAccount(user.getName());
        if (loginUser == null){
            LOG.error("not find the login user by account:" + user.getName());
        } else {
            session.setAttribute("loginUser", loginUser);
        }

        return loginUser;
    }

    @RequestMapping("/resource")
    @ResponseBody
    public Map<String, Object> home() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

}
