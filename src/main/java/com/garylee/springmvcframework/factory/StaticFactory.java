package com.garylee.springmvcframework.factory;

import com.garylee.springmvcframework.utils.Config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GaryLee on 2018-08-16 22:52.
 */
public class StaticFactory {
    private static Map<String,String> staticMap;
    private static String staticPath = Config.projectPath+"\\src\\main\\resources\\static";
    public static void getStatic(){
        staticMap = new HashMap<>();
        File file = new File(staticPath);
        initStatic(file,staticMap);
    }
    public static void initStatic(File file,Map<String,String> staticMap){
        for(File f:file.listFiles()){
            if(f.isDirectory()){
                initStatic(f,staticMap);
            }else {
                String key = f.getName();
                String str = null;
                StringBuffer value = new StringBuffer();
                try(BufferedReader bufferedReader = new BufferedReader(new FileReader(f))){
                    while((str=bufferedReader.readLine())!=null){
                        if(value.length()==0)
                            value.append(str);
                        else
                            value.append(System.lineSeparator()+str);
                    }
                }catch (Exception e){

                }
                staticMap.put(key,value.toString());
            }
        }
    }
    public static File parseStatic(String url){
        File file = new File(staticPath,url);
//        System.out.println("exists?"+file.exists());
        return file;
    }
    public static void main(String[] args) {
//        getStatic();
//        System.out.println(staticMap);
        System.out.println(parseStatic("abc.txt"));
    }
}
