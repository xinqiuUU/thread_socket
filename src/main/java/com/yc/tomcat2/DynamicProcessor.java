package com.yc.tomcat2;

import com.yc.tomcat2.servlet.YcServlet;
import com.yc.tomcat2.servlet.YcServletContext;
import com.yc.tomcat2.servlet.YcServletRequest;
import com.yc.tomcat2.servlet.YcServletResponse;
import com.yc.tomcat2.servlet.http.YcHttpServletRequest;

import java.io.PrintWriter;

public class DynamicProcessor implements Processor{

    @Override
    public void process(YcServletRequest request, YcServletResponse response) {
        //request  中的参数已经解析好了
        //1.从request中取出requestURI  ( /hello  到ServletContext的map中去取class,
        String url = ( (YcHttpServletRequest)request).getRequestURI();

        int contextPathIndex = ( (YcHttpServletRequest)request).getContextPath().length();
        url = url.substring( contextPathIndex );
        try {
            //2. 为了保证单例  先看 另一个map中是否已经有这个class 的 实例  a.如有 ，说明是第二次访问 ，再调用 service()
            YcServlet servlet = null;
            if (YcServletContext.servletInstance.containsKey(url)) {
                servlet = YcServletContext.servletInstance.get(url);
            } else {
                // b.没有 ，说明此servlet是第一次访问 ，
                //     先利用反射创建servlet(调用servlet的无参构造方法)，再调用 init() -> service()

                Class clz = YcServletContext.servletClass.get(url);
                Object obj = clz.newInstance();// 调用次servlet的构造 方法
                if (obj instanceof YcServlet) {
                    servlet = (YcServlet) obj;
                    servlet.init();
                    YcServletContext.servletInstance.put(url, servlet);
                }
            }
            // 此 servlet 就是客户端要访问的 servlet HelloServlet
            servlet.service(request, response);//->  YcHttpServlet.service() -> 根据method调用 doXxx()
            //          还要考虑些 servlet 执行失败的情况 则抛出  500错误 响应给客户端
        }catch (Exception e){
            String bodyEntity = e.toString();
            String protocol = gen500( bodyEntity );
            //以输出流返回到客户端
            PrintWriter out = response.getWriter();
            out.print( protocol ); // 协议头
            out.println(  bodyEntity );//协议实体 即错误信息
            out.flush();
        }
    }

    private String gen500(String bodyEntity) {
        String protocol500 = "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/html\r\nContent-Length: " + bodyEntity.getBytes().length + "\r\n\r\n";
        return protocol500;
    }
}
