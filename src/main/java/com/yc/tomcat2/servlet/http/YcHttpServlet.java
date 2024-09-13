package com.yc.tomcat2.servlet.http;

import com.yc.tomcat2.servlet.YcServlet;
import com.yc.tomcat2.servlet.YcServletRequest;
import com.yc.tomcat2.servlet.YcServletResponse;

public abstract class YcHttpServlet implements YcServlet {

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
    protected void doGet(YcHttpServletRequest request, YcHttpServletResponse response) {

    }
    protected void doPost(YcHttpServletRequest request, YcHttpServletResponse response) {

    }
    protected void doHead(YcHttpServletRequest request, YcHttpServletResponse response) {

    }
    protected void doDelete(YcHttpServletRequest request, YcHttpServletResponse response) {

    }
    protected  void doTrace(YcHttpServletRequest request, YcHttpServletResponse response) {

    }
    protected void doOption(YcHttpServletRequest request, YcHttpServletResponse response) {

    }

    /*
       模板设计模式 规范 httpservlet中各方法的调用顺序
     */
    public void service(YcServletRequest request, YcServletResponse response) {
        //从request中取出method(http协议特有)
        String method = ((YcHttpServletRequest)request).getMethod();
        if ("get".equalsIgnoreCase(  method ) ){
            doGet((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("post".equalsIgnoreCase(  method ) ){
            doPost((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("head".equalsIgnoreCase(  method ) ){
            doHead((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("delete".equalsIgnoreCase(  method ) ){
            doDelete((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("trace".equalsIgnoreCase(  method ) ){
            doTrace((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("option".equalsIgnoreCase(  method ) ){
            doOption((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else {
            //TODO 错误的响应协议

        }
    }
    public void service(YcHttpServletRequest request, YcHttpServletResponse response) {
       service(  request , response);
    }
}
