package com.garylee.springmvcframework.factory;

import com.garylee.springmvcframework.annotation.Controller;
import com.garylee.springmvcframework.annotation.RequestMapping;
import com.garylee.springmvcframework.annotation.ResponseBody;
import com.garylee.springmvcframework.domain.MethodMap;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by GaryLee on 2018-08-15 01:11.
 */

public class ClassFactory {
    // TODO: 2018/8/19 0019 考虑是要扫src包还是target包
//    private static String projectPath = "com\\garylee\\springmvcframework";//包路径
    private static String projectPath = "com\\garylee\\springmvcframework";//包路径(修改为src)
    private static Set<Class<?>> set = new HashSet<>();//存放该项目包下所有类
//    private static Map<String,String> htmlMap;//视图定位,存放html名(不包含后缀),返回对应的html文件
    private static Map<String,MethodMap> methodMap;//key为url,value为映射的方法
    private static Map<String,Class> classMap;//key为url,value为映射到的类

    private static String path = System.getProperty("user.dir") + "\\src\\main\\java\\com\\garylee\\springmvcframework";
    public static void scan(){
        initSet(path);//扫描所有类并存放到set中
//        System.out.println(set);//输出所有的java文件(该项目内)
        methodMap = new HashMap<>();
        classMap = new HashMap<>();
        for(Class clazz:set){
//            System.out.println(clazz);//输出类
            if(clazz.isAnnotationPresent(Controller.class)){
                Method[] methods = clazz.getDeclaredMethods();
                for(Method method:methods){
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        boolean isBody = false;
                        if(method.isAnnotationPresent(ResponseBody.class)){
                            isBody = true;
                        }
                        //methodMap存放当前URL和对应
                        MethodMap method1 = new MethodMap(requestMapping.value(),requestMapping.method(),isBody,method);
                        methodMap.put(requestMapping.value(),method1);
                        System.out.println(requestMapping.value()+"初始化成功!");

                        classMap.put(requestMapping.value(),clazz);
                    }
                }
            }
        }
    }
    public static void main(String[] args){
        initSet(path);
    }
    private static void initSet(String path){
        File[] files = new File(path).listFiles();
        for(File file:files){
            //如果是文件夹，则递归调用(即进行子文件夹)
            if(file.isDirectory())
                initSet(file.getAbsolutePath());
            if(file.getName().contains(".java")){
                String paths = file.getAbsolutePath();
                //文件名(包括package,如com.garylee.springmvcframework.annotation.Controller)
                String className = paths.substring(paths.indexOf(projectPath)).replace(".java","").replace(File.separator,".");
//                System.out.println(className);//Class.forName的值
//                System.out.println(Class.forName(className));
                try {
                    set.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static Set<Class<?>> getSet() {
        return set;
    }

    public static void setSet(Set<Class<?>> set) {
        ClassFactory.set = set;
    }

    public static Map<String, MethodMap> getMethodMap() {
        return methodMap;
    }

    public static void setMethodMap(Map<String, MethodMap> methodMap) {
        ClassFactory.methodMap = methodMap;
    }

    public static Map<String, Class> getClassMap() {
        return classMap;
    }

    public static void setClassMap(Map<String, Class> classMap) {
        ClassFactory.classMap = classMap;
    }
}
