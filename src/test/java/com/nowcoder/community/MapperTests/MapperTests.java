package com.nowcoder.community.MapperTests;


import com.nowcoder.community.CommunityApplication;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        User liubei = userMapper.selectByName("liubei");
        System.out.println(liubei);

        User user1 = userMapper.selectByEmail("nowcoder103@sina.com");
        System.out.println(user1);



    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("Leo");
        user.setPassword("1996");
        user.setEmail("1996@qq.com");
        user.setSalt("abc");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());


    }

    @Test
    public void testUpdateUser(){
        int i = userMapper.updateStatus(150, 1);
        System.out.println(i);

        int i1 = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(i1);

        int i2 = userMapper.updatePassword(150, "192");
        System.out.println(i2);
    }


    @Test
    public void testSelectPosts(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost post :discussPosts){
            System.out.println(post);
        }
        int i = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(i);
    }

}
