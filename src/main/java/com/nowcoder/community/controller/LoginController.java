package com.nowcoder.community.controller;


import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${server.servlet.context-path}")
    private String context_path;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Producer kaptchaProducer;

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


    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response/*, HttpSession session*/) {
        // ???????????????
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // ??????????????????session
//        session.setAttribute("kaptcha", text);

        //??????????????????
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(context_path);
        response.addCookie(cookie);
        //??????????????????redis
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(kaptchaKey,text,60, TimeUnit.SECONDS);

        // ???????????????????????????,????????????????????????????????????
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("?????????????????????:" + e.getMessage());
        }
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, HttpServletResponse response, /*HttpSession session, */String username, String password, String code, boolean rememberme, @CookieValue("kaptchaOwner") String kaptchaOwner){
//        String kaptcha = (String)session.getAttribute("kaptcha");
        String kaptcha = null;
        if(StringUtils.isNotBlank(kaptchaOwner)){
            String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(kaptchaKey);
        }

        if(!code.equalsIgnoreCase(kaptcha)|| StringUtils.isBlank(code)||StringUtils.isBlank(kaptcha)){
            model.addAttribute("codeMsg","The code is not correct");
            return "/site/login";
        }
        //?????????????????????
        int expiredSeconds;
        expiredSeconds = rememberme?CommunityConstant.REMEMBERME_EXPIRED_SECONDS:CommunityConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(context_path);
            cookie.setMaxAge(expiredSeconds);

            response.addCookie(cookie);
            logger.info("??????cookie....");
            return "redirect:/index";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }
}
