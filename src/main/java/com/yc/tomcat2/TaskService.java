package com.yc.tomcat2;

import com.yc.tomcat2.servlet.YcServletContext;
import com.yc.tomcat2.servlet.http.YcHttpServletRequest;
import com.yc.tomcat2.servlet.http.YcHttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/*
    任务 类
 */
public class TaskService implements Runnable{
    private Socket socket;
    private InputStream iis;
    private OutputStream oos;
    private boolean flag = true;
    private Logger log = Logger.getLogger( TaskService.class );

    public TaskService(Socket socket) {
        this.socket = socket;
        try{
            this.iis = socket.getInputStream();
            this.oos = socket.getOutputStream();
        }catch (Exception e){
            log.error( "socket联接失败。。。");
            e.printStackTrace();
            flag = false;
        }
    }

    @Override
    public void run() {
//        do {
//            //通过socket的InputStream读取客户端发送过来的消息 解析
//            //处理请求的资源
//            //返回响应
//
//        }while();
        //结束(socket.close();)
        if ( this.flag ){
            //HttpServletRequest 中解析出所有 的 请求信息 ( method, 资源地址url, header http版本 头域(referre,user-agent), 参数parameter )
            //存在 HttpServletRequest 对象中
//            YcHttpServletRequest request = new YcHttpServletRequest( s,iis );
//            //响应  本地地址 + 资源地址url 读取文件 拼接http响应 以流的形式返回给客户端
//            YcHttpServletResponse response = new YcHttpServletResponse( request , oos );
//            response.send();
            YcHttpServletRequest request = new YcHttpServletRequest( socket ,  iis );
            YcHttpServletResponse response = new YcHttpServletResponse( request , oos );
            //根据  request 中的requestURI 来判断 什么 资源
            Processor processor = null;
            // requestURL: /wowotuan/hello - contextPath:/wowotan => 结果
            int contextPathIndex = request.getContextPath().length();
            String url = request.getRequestURI().substring( contextPathIndex );
            if (  YcServletContext.servletClass.containsKey( url ) ){
                //这是动态请求
                processor = new DynamicProcessor();
            }else {
                //这是静态请求 即静态页面
                processor = new StaticProcessor();
            }
            processor.process( request, response );


        }
        try {
            this.iis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
