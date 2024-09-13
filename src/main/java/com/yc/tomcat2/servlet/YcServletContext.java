package com.yc.tomcat2.servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    应用程序上下文类  常量类
 */
public class YcServletContext {
    /*
        <String,Class>
        url地址  servlet的字节码路径
        request  ->   再利用反射  实例化对象
        //  键:  /hello
     */
    public static Map<String,Class> servletClass= new ConcurrentHashMap<>();

    /*
        每个servlet都是单例  当第一次访问这个servlet时  创建后保存到map中 实例
     */
    public  static Map<String,YcServlet> servletInstance= new ConcurrentHashMap<>();

}
