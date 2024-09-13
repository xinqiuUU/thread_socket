package com.yc.tomcat2.wowotuan;

import com.yc.tomcat2.servlet.YcWebServlet;
import com.yc.tomcat2.servlet.http.YcHttpServlet;
import com.yc.tomcat2.servlet.http.YcHttpServletRequest;
import com.yc.tomcat2.servlet.http.YcHttpServletResponse;

@YcWebServlet("/bye")
public class ByeServlet extends YcHttpServlet {

    public ByeServlet() {
        System.out.println("ByeServlet构造方法");
    }

    @Override
    public void init() {
        System.out.println("HelloServlet初始化方法");
    }

    @Override
    protected void doGet(YcHttpServletRequest req, YcHttpServletResponse resp) {
        System.out.println("Bye HelloServlet doGet方法");

    }
}
