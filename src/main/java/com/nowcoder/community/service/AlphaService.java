package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")

public class AlphaService {

    @Autowired
    private AlphaDAO alphaDAO;

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
}
