package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")

public class AlphaController {
    @Autowired
    private AlphaService alphaService;


    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }



    @RequestMapping("/hello")
    @ResponseBody
    public String savHello(){
        return "Hello Spring boot";
    }


    @RequestMapping("/http")
    public void http(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        System.out.println(httpServletRequest.getMethod());
        System.out.println(httpServletRequest.getServletPath());
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String s = headerNames.nextElement();
            String header = httpServletRequest.getHeader(s);
            System.out.println(s + ":" + header);
        }

        System.out.println(httpServletRequest.getParameter("code"));

        httpServletResponse.setContentType("/text/html;charset=utf-8");
        try{
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write("<h1>nowcoder</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(path="/students",method = RequestMethod.GET)
    @ResponseBody
    //students?current=1&limit=20
    public String students(
            @RequestParam(name="current",required = false,defaultValue = "1") int current,
            @RequestParam(name="limit",required = false,defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";

    }
    //find a student who id is 123
    //student/123
    @RequestMapping(path="/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(
            @PathVariable("id") int id
    ){
        System.out.println(id);
        return "a student";

    }

    //POST
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){
        System.out.println(name);
        System.out.println(age);

        return "success";
    }


    //response html to chrome
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","Leo");
        modelAndView.addObject("age",26);
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","shenzhen university");
        model.addAttribute("age","38");
        return "/demo/view";
    }

    //response json data(yi bu request)
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp(){
        Map<String, Object> map = new HashMap<>();
        map.put("name","Leo");
        map.put("age",25);
        map.put("salary",25000);
        return map;
    }

    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps(){
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("name","Leo");
        map.put("age",25);
        map.put("salary",25000);
        list.add(map);

        map = new HashMap<>();
        map.put("name","Mike");
        map.put("age",22);
        map.put("salary",22000);
        list.add(map);

        return list;
    }

    //Cookie example
    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookies(HttpServletResponse response){
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        cookie.setPath("/community/alpha");
        cookie.setMaxAge(60*10);
        response.addCookie(cookie);
        return "set Cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookies(@CookieValue("code") String code){
        System.out.println(code);
        return "get Cookie";
    }


    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id",1);
        session.setAttribute("name","leo");
        return "set Session";
    }

    @RequestMapping(path="/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get Session";
    }

    //ajax示例
    @RequestMapping(path = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJsonString(0,"操作成功");
    }


}
