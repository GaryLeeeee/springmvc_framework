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
    @RequestMapping("/url")
    public void index(){
        System.out.println("this is index!");
    }
    @RequestMapping("/getOne")
    @ResponseBody
    public int get(int id){
        return id*2;
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
