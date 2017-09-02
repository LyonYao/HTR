package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.UserPasswordHelper;
import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.User;
import com.htr.loan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @RequestMapping(value = "/{active}", method = RequestMethod.GET)
    public List<User> findAllUser(@PathVariable int active){
        if (active == 0){
            return userService.findAllUser();
        } else return userService.findAllByActiveTrue();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user, false);
    }

    @RequestMapping(value = "stop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> stopUser(@RequestBody List<User> users){
        boolean isDeleted = userService.activeOrStopUsers(users, false);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }

    @RequestMapping(value = "active", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> activeUser(@RequestBody List<User> users){
        boolean isDeleted = userService.activeOrStopUsers(users, true);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }

    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> changePassword(@RequestBody UserPasswordHelper userPasswordHelper, HttpSession session){
        User currentUser = (User)session.getAttribute(Constants.SESSION_USER_KEY);
        Map<String, String> result = new HashMap<>();
        if(!bCryptPasswordEncoder.matches(userPasswordHelper.getOriginalPassword(), currentUser.getPassword())){
            result.put(Constants.RESPONSE_CODE, Constants.CODE_FAIL);
            result.put(Constants.RESPONSE_MSG, "原始密码输入错误!");
        } else if(!userPasswordHelper.getNewPassword().equals(userPasswordHelper.getRepeatPassword())){
            result.put(Constants.RESPONSE_CODE, Constants.CODE_FAIL);
            result.put(Constants.RESPONSE_MSG, "两次输入的密码不匹配!");
        } else {
            currentUser.setPassword(userPasswordHelper.getNewPassword());
            userService.saveUser(currentUser, true);
            result.put(Constants.RESPONSE_CODE, Constants.CODE_SUCCESS);
            result.put(Constants.RESPONSE_MSG, "修改成功!");
        }
        return result;
    }

}
