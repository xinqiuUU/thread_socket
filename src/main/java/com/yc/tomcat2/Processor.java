package com.yc.tomcat2;

import com.yc.tomcat2.servlet.YcServletRequest;
import com.yc.tomcat2.servlet.YcServletResponse;

/*
    资源处理接口
 */
public interface Processor {

    //处理方法
    public void process(YcServletRequest request, YcServletResponse response);

}
