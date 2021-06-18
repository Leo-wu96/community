package com.nowcoder.community.controller;


import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    @RequestMapping(path="/register",method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(map==null||map.isEmpty()){
            model.addAttribute("msg","Register success, please activate soon");
            model.addAttribute("target","/index");
            return "/site/operate-result";

        }else{
            model.addAttribute("usernamemsg",map.get("usernameMsg"));
            model.addAttribute("passwordmsg",map.get("passwordMsg"));
            model.addAttribute("emailmsg",map.get("emailMsg"));

            return "/site/register";
        }
    }



    //http://localhost:8080/community/activation/user/code
    @RequestMapping(path="/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model,@PathVariable("userId") int userId,@PathVariable("code") String code){
        int activation = userService.activation(userId, code);
        if(activation==ACTIVATION_SUCCESS) {
            model.addAttribute("msg","Activation success, jump to the sign in");
            model.addAttribute("target","/login");
        }else if(activation==ACTIVATION_REPEAT){
            model.addAttribute("msg","Activation has been done before");
            model.addAttribute("target","/index");

        }else {
            model.addAttribute("msg","Activation code error");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
}
