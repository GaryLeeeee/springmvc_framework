package com.garylee.springmvcframework.domain;

import java.lang.reflect.Method;

/**
 * Created by GaryLee on 2018-08-16 18:30.
 */
public class MethodMap {
    private String name;
    private String url;
    private String type;
    private boolean isBody;
    private Method method;
    public MethodMap(String url, String type, boolean isBody, Method method) {
        this.name = method.getName();
        this.url = url;
        this.type = type;
        this.isBody = isBody;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBody() {
        return isBody;
    }

    public void setBody(boolean body) {
        isBody = body;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "MethodMap{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", isBody=" + isBody +
                ", method=" + method +
                '}';
    }
}
