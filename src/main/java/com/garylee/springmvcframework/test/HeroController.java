package com.garylee.springmvcframework.test;

import com.garylee.springmvcframework.annotation.Controller;
import com.garylee.springmvcframework.annotation.RequestMapping;
import com.garylee.springmvcframework.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * Created by GaryLee on 2018-08-15 18:02.
 */
@Controller
public class HeroController {
    @RequestMapping("/login")
    @ResponseBody
    public String login(String username,String password){
        System.out.println("用户:"+username+",密码:"+password+"---登陆成功!");
        return "index~~";//有@ResponseBody的话就是直接输出该字符串
    }
//    @RequestMapping("/getOne")
//    @ResponseBody
//    public int get(int id){
//        System.out.println(id*3);
//        return id*2;
//    }
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    public static void main(String[] args) {
        try {
            Class aClass = Class.forName("com.garylee.springmvcframework.test.HeroController");
            Method[] methods = aClass.getDeclaredMethods();
            System.out.println(methods[0].getName());
//            Object object = aClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
            Object object = aClass.newInstance();
            methods[0].invoke(object,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
