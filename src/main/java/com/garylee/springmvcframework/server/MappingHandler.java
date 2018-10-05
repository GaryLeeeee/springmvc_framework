package com.garylee.springmvcframework.server;

import com.garylee.springmvcframework.domain.MethodMap;
import com.garylee.springmvcframework.factory.ClassFactory;
import com.garylee.springmvcframework.factory.HtmlFactory;
import com.garylee.springmvcframework.factory.StaticFactory;
import com.garylee.springmvcframework.test.HeroController;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        ClassFactory.scan();//初始化数据
        methodMap = ClassFactory.getMethodMap();//存放Map<url,MethodMap>
        classMap = ClassFactory.getClassMap();//存放url和对应的类

        htmlMap = HtmlFactory.getHtml();//初始化模板
        System.out.println("mapping初始化完毕!");
    }
    //处理
    public static void handler(String url,String params, PrintWriter printWriter, PrintStream printStream){
        if(url.contains("?"))
            url = url.substring(0,url.indexOf("?"));
        MethodMap methodName = methodMap.get(url);
        try {
            //请求的是文件，则返回静态文件(有后缀名的)
            if(url.contains(".")){
                System.out.println("用户请求静态资源:"+url);
                File file = StaticFactory.parseStatic(url);//通过路径+文件名构造一个File对象
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
                //此处已判断请求资源为静态资源，故不用进行mapping映射
                return;//!!!!!使用return可以强制中断代码，使后续代码不再执行(类似while的break)
            }
            Class clazz = classMap.get(url);//通过url确定是哪个controller
            Object object = clazz.newInstance();//通过controller对应的类来实例化一个object
            // TODO: 2018/8/19 0019 当需要参数的时候应该怎么赋值？ 
//            methodName.getMethod().invoke(object,null);
            //当没有ResponseBody，则返回值为String，并解析为跳转路径
            //否则，将返回指定的数据类型，如json
            if(!methodName.isBody()){
                /**
                 *  invoke参数(obj,...args)
                 *  obj - 从中调用底层方法的对象
                 args - 用于方法调用的参数
                 其中...args表示可以传任意个参数，将会转换为一个数组存储，此处的...args表示要调用方法的参数(不定)
                 */
                String fileName = methodName.getMethod().invoke(object).toString();
                if(htmlMap.containsKey(fileName)){
                    String html = htmlMap.get(fileName);
//                    System.out.println("html:"+html);//输出即将返回的templates(html)的内容
                    printWriter.println("HTTP/1.1 200 OK");
                    printWriter.write(html);
                    printWriter.flush();
                }else {
                    System.err.println("未找到"+url+"对应的method!请检查htmlMap的完整性!");
                    printWriter.println("HTTP/1.1 200 OK");
                    printWriter.println("Content-type:text/html;charset=utf-8");//乱码则加上这行
                    printWriter.println();//header和body间有个空行(注意!)
                    printWriter.println("<h1 style='color:red'>未找到"+url+"对应的templates!请检查htmlMap的完整性!</h1>");
                    printWriter.flush();
                }
            }else{
                // TODO: 2018/10/4 0004 如果输入的参数不是按顺序的话，如何根据参数与方法形参名对应，或使用注解类似@Param
                //方法参数，用Object[]存放，会对应方法的形参
                Object o = methodName.getMethod().invoke(object,parseParams(params));
//                System.out.println(o);
                printWriter.println("HTTP/1.1 200 OK");
                printWriter.println("Content-type:text/html;charset=utf-8");//乱码则加上这行
                printWriter.println();//header和body间有个空行(注意!)
                printWriter.println(o);//输出Controller中的ResponseBody的ajax返回数据
                printWriter.flush();
            }
            // TODO: 2018/10/3 0003 传递参数的存储，post则获取post内容，get获取url的?后的信息。数据用于方法参数调用，可参考@Param 
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("没找到映射"+url);
            printWriter.println("HTTP/1.1 404 fileNotFound");
            printWriter.write(htmlMap.get("error"));
            printWriter.flush();
        }
    }

    // TODO: 2018/10/5 0005 目前只能实现String类型的参数，后期可以通过类型转换或其他方式实现 
    //将login?username=root&password=admin转换为Object[]{root,admin},因为这里只需要取value
    public static Object[] parseParams(String params){
        ///login?username=root&password=admin
        List<Object> list = new ArrayList<>();
        String temp;
        //login?username=root&password=admin
        //第一次取root
        //第二次取admin
        while(true){
            temp = params.substring(params.indexOf("=")+1,!params.contains("&")?params.length():params.indexOf("&"));
            list.add(temp);
            if(!params.contains("&"))
                break;
            params = params.substring(params.indexOf("&")+1);
        }
        return list.toArray();//List转数组
    }

    public static void main(String[] args) {
//        System.out.println(parseParams("username=root&password=admin"));
        Object[] objects = parseParams("login?username=root&password=admin");
        System.out.println("/login?d".substring(0,"/login?6".indexOf("?")));
//        handler("/url");//！！！注意加上/
    }
}
