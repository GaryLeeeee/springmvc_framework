package com.garylee.springmvcframework.server;

import com.garylee.springmvcframework.domain.MethodMap;
import com.garylee.springmvcframework.factory.ClassFactory;
import com.garylee.springmvcframework.factory.HtmlFactory;
import com.garylee.springmvcframework.factory.StaticFactory;
import com.garylee.springmvcframework.test.HeroController;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
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
    public static Map<String,String> staticMap;
    //静态方法(第一次调用该类则会调用，用于初始化map)
    static{
        System.out.println("mapping开始初始化...");
        ClassFactory.scan();
        methodMap = ClassFactory.getMethodMap();
        classMap = ClassFactory.getClassMap();

        htmlMap = HtmlFactory.getHtml();
        System.out.println("mapping初始化完毕!");
    }
    public static void handler(String url, PrintWriter printWriter, PrintStream printStream){
        MethodMap methodName = methodMap.get(url);
        try {
            if(url.contains(".")){
                System.out.println("用户请求静态资源:"+url);
                File file = StaticFactory.parseStatic(url);
                if(file.exists()){
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[(int) file.length()];
                    printStream.println("HTTP/1.1 200 OK");
                    printStream.println("");
                    while ((fileInputStream.read(bytes)) != -1) {
                        printStream.write(bytes);
                    }
                }else {
                    System.out.println("静态资源"+url+"不存在!");
                }
                return;//!!!!!使用return可以强制中断代码，使后续代码不再执行(类似while的break)
            }
            Class clazz = classMap.get(url);//通过url确定是哪个controller
            Object object = clazz.newInstance();//通过controller对应的类来实例化一个object
            // TODO: 2018/8/19 0019 当需要参数的时候应该怎么赋值？ 
//            methodName.getMethod().invoke(object,null);
            if(!methodName.isBody()){
                String fileName = methodName.getMethod().invoke(object,null).toString();
                if(htmlMap.containsKey(fileName)){
                    String html = htmlMap.get(fileName);
                    System.out.println("html:"+html);
                    printWriter.println("HTTP/1.1 200 OK");
                    printWriter.write(html);
                    printWriter.flush();
                }else {
                    System.err.println("未找到"+url+"对应的method!请检查htmlMap的完整性!");
                }
            }else{
                Object o = methodName.getMethod().invoke(object,null);
                System.out.println(o);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("没找到映射"+url);
            printWriter.println("HTTP/1.1 404 fileNotFound");
            printWriter.write(htmlMap.get("error"));
            printWriter.flush();
        }
    }

    public static void main(String[] args) {
//        handler("/url");//！！！注意加上/
    }
}
