package com.garylee.springmvcframework.server;

import com.garylee.springmvcframework.factory.HtmlFactory;
import com.garylee.springmvcframework.utils.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GaryLee on 2018-08-10 22:27.
 */
public class HttpServer implements Runnable{
    private ServerSocket serverSocket = null;
//    private Socket socket = null;
    private BufferedReader bufferedReader = null;
    private PrintWriter printWriter = null;//用于返回html文件内容，用字符流就可以
    private PrintStream printStream = null;//用于返回静态文件，需要用流进行传输
    private String method = null;
    private String path = null;
    private Map<String,String> socketValues = null;
    File file = new File(Config.projectPath+"\\src\\main\\resources\\templates");
    int i = 0;
    public HttpServer(){
        try {
            serverSocket = new ServerSocket(1111);
            System.out.println("正在监听1111端口...");
            new Thread(this).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true) {
            try(Socket socket = serverSocket.accept()){
//                socket = serverSocket.accept();
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                socketValues = new HashMap<>();
                String content;
                while ((content = bufferedReader.readLine()) != null && !content.isEmpty()) {
                    //如果是图标,则不读取header和body
                    if(content.contains("/favicon.ico"))
                        break;
                    System.out.println(content);
                    if(content.endsWith("HTTP/1.1")){
                        //判断是否为请求行
                        String[] parts = content.split(" ");
                        method = parts[0];//request的方法(get,post等)
                        path = parts[1];
                    }else {
                        //请求头(header)
                        String[] strings = content.split(": ");
                        socketValues.put(strings[0], strings[1]);
                    }
                }
                StringBuffer stringBuffer = new StringBuffer();
                //post时body部分不为空，但没有换行，所以readline不能读到，所以用read一个个读取
                if(method.equalsIgnoreCase("POST")){
                    int contentLength = Integer.parseInt(socketValues.get("Content-Length"));
                    for(int i=0;i<contentLength;i++){
                        stringBuffer.append((char)bufferedReader.read());
                    }
                    System.out.println("post信息为:"+stringBuffer);
                }
                //如果是图标,则不返回response
                if(content.contains("/favicon.ico"))
                    continue;
                System.out.println("");
                printWriter = new PrintWriter(socket.getOutputStream());
                printStream = new PrintStream(socket.getOutputStream());
                //传参数,get的话就传url的?后面的,post就传参数部分
                String params = (method.equalsIgnoreCase("GET"))?path:stringBuffer.toString();
                MappingHandler.handler(path,params,getPrintWriter(),getPrintStream());//处理请求的url,获取到对应的class和method并invoke

            } catch (Exception e) {

            }
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public static void main(String[] args) {
        new HttpServer();
    }


}
