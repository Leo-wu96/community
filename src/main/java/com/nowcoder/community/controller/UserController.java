package com.nowcoder.community.controller;


import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.GenericArrayType;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;


    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    @LoginRequired
    public String getSettingPage(){
        return "/site/setting";
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    @LoginRequired
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage==null){
            model.addAttribute("error","您还没有选择图片");
            return "redirect:/setting";
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不对");
            return "redirect:/setting";
        }
        filename = CommunityUtil.generateUUID() + suffix;
        File des = new File(uploadPath + '/' + filename);
        try {
            //存入文件
            headerImage.transferTo(des);
        } catch (IOException e) {
            logger.error("上传文件失败："+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！",e);
        }
        //更新用户头像访问路径
        //http:localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        userService.updateHeader(user.getId(),domain+contextPath+"/user/header/"+filename);
        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务器存放的位置
        fileName = uploadPath+'/'+fileName;
        //response发送文件需要获取其后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType(suffix);

        try (
                OutputStream outputStream = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
                ){
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len=fis.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
        } catch (IOException e) {
            logger.error("读取头像失败："+e.getMessage());
        }
    }


    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @LoginRequired
    public String updatePassword(String originPassword, String newPassword, String confirmPassword, Model model){
        if(originPassword.isEmpty()){
            model.addAttribute("error","原密码为空");
            return "redirect:/setting";
        }

        if(newPassword.isEmpty()){
            model.addAttribute("error","新密码为空");
            return "redirect:/setting";
        }

        if(confirmPassword.isEmpty()){
            model.addAttribute("error","确认密码为空");
            return "redirect:/setting";
        }

        User user = hostHolder.getUser();
        String password = CommunityUtil.md5(originPassword + user.getSalt());
        if(!password.equals(user.getPassword())){
            model.addAttribute("error","原密码错误");
            return "redirect:/setting";
        }
        if(!newPassword.equals(confirmPassword)){
            model.addAttribute("error","密码不一致");
            return "redirect:/setting";
        }
        password = CommunityUtil.md5(newPassword+user.getSalt());
        userService.updatePassword(user.getId(),password);
        logger.info("密码修改成功");

        return "redirect:/index";
    }

    // 个人主页
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }






}
