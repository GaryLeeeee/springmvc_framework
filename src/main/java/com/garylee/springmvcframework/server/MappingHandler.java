package com.garylee.springmvcframework.server;

import com.garylee.springmvcframework.domain.MethodMap;
import com.garylee.springmvcframework.factory.ClassFactory;
import com.garylee.springmvcframework.factory.HtmlFactory;
import com.garylee.springmvcframework.test.HeroController;
import com.sun.xml.internal.bind.v2.TODO;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GaryLee on 2018-08-19 01:12.
 */
public class MappingHandler {
    public static Map<String,String> htmlMap;
    public static Map<String,MethodMap> methodMap;
    public static Map<String,Class> classMap;
    //静态方法(第一次调用该类则会调用，用于初始化map)
    static{
        System.out.println("mapping开始初始化...");
        ClassFactory.scan();
        methodMap = ClassFactory.getMethodMap();
        classMap = ClassFactory.getClassMap();

        htmlMap = HtmlFactory.getHtml();
        System.out.println("mapping初始化完毕!");
    }
    public static void handler(String url){
        MethodMap methodName = methodMap.get(url);
        try {
            Class clazz = classMap.get("/url");//通过url确定是哪个controller
            Object object = clazz.newInstance();//通过controller对应的类来实例化一个object
            // TODO: 2018/8/19 0019 当需要参数的时候应该怎么赋值？ 
            methodName.getMethod().invoke(object,null);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("没找到映射"+"/url");
        }
    }

    public static void main(String[] args) {
        handler("/url");//！！！注意加上/
    }
}
