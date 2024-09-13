package com.yc.tomcat2.servlet;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
    注解
 */
@Target({ElementType.TYPE})  //Type 表示这个注解是作用于类上的 接口
@Retention(RetentionPolicy.RUNTIME) // 注解的生命周期  运行时
public @interface YcWebServlet {

    String value() default "";

}

//使用一 : @YcWebServlet(value = "/login")
// 使用二 : @YcWebServlet("/login")
        // public class LoginServlet extends YcHttpServlet{}