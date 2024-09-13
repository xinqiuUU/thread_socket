package com.yc.tomcat2.wowotuan;


import com.yc.tomcat2.servlet.YcWebServlet;
import com.yc.tomcat2.servlet.http.YcHttpServlet;
import com.yc.tomcat2.servlet.http.YcHttpServletRequest;
import com.yc.tomcat2.servlet.http.YcHttpServletResponse;

import java.io.PrintWriter;

@YcWebServlet("/hello") // 注解的方式配置servlet
public class HelloServlet extends YcHttpServlet {

    public  HelloServlet() {
        System.out.println("HelloServlet构造方法");
    }

    @Override
    public void init() {
        System.out.println("HelloServlet初始化方法");
    }

    @Override
    protected void doGet(YcHttpServletRequest req, YcHttpServletResponse resp) {
//        System.out.println("HelloServlet doGet方法");
        String result = "hello,world 你好！";
        PrintWriter out = resp.getWriter();
        //TODO 标准的tomcat是由tomcat服务器来完成这个响应的构建
        //response.setCharacterEncoding("utf-8");
        out.print("HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: " + result.getBytes().length + "\r\n\r\n");
        out.println(result);
        out.flush();
    }
}
