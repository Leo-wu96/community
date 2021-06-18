package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDAO;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
//@Scope("prototype")

public class AlphaService {

    @Autowired
    private AlphaDAO alphaDAO;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public AlphaService(){
        System.out.println("construct AlphaService");
    }

    @PostConstruct
    public void init(){
        System.out.println("initilizer AlphaService");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("destroy the AlphaService");
    }


    public String find(){
        return alphaDAO.select();
    }



    // REQUIRED:支持当前事务（外部事务），如果不存在则创建新任务
    // REQUIRED_NEW:创建一个新事务，并且暂停当前事务（外部事务）
    // NESTED:如果当前存在事务（外部事务），则嵌套在该事务中执行（独立的回滚和提交），否则就和required一样
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Object save1(){
        // add user
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        user.setEmail("alpha@qq.com");
        userMapper.insertUser(user);

        // add post
        DiscussPost post = new DiscussPost();
        post.setCreateTime(new Date());
        post.setUserId(user.getId());
        post.setTitle("hello");
        post.setContent("welcome join in nowcoder");
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");

        return "ok";
    }
}
