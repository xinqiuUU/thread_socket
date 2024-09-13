package com.yc;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Test3_Text {
    public static void main(String[] args) throws IOException, InterruptedException {
        final Logger log = Logger.getLogger(Test3_Server.class);
        final int port = 10000;
        ServerSocket ss = null;
        ss = new ServerSocket( 10002 );
        //用 info 级别记录  ServerSocket套接字创建成功的信息
        log.info( ss.getInetAddress().getHostAddress()+"启动了，监听了端口号:***"+ss.getLocalPort()+"***...");
        while(true){
            log.info("服务器ServerSocket准备等待客户端的连接");
            Socket socket = ss.accept();//阻塞式方法
            log.info("获取了一个与客户端:"+socket.getRemoteSocketAddress() +"的联接");

            //TODO  服务器中的业务
            List<String> list = new ArrayList<>();
            String time = "好好好";
                try(OutputStream oos = socket.getOutputStream();
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(oos, "UTF-8"), true)) {
                    out.println(time);
                }catch (Exception e){
                    e.printStackTrace();
                }
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(time);

            socket.close();
            log.info("服务器断开与客户端的联接");
            System.out.println("服务器断开与客户端的联接");
        }
    }



}
