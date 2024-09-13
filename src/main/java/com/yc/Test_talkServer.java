package com.yc;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/*
    点对点的服务端与客户端的聊天
 */
public class Test_talkServer {
    static  final Logger log = Logger.getLogger(Test_talkServer.class);

    public static void main(String[] args) {
        ServerSocket ss = null;
        for ( int i=10000;i<=65535;i++){
            try{
                //选择合适端口的工作
                ss = new ServerSocket( i );
                break;
            }catch (Exception e){
                e.printStackTrace();
                if (e instanceof BindException){
                    //用错误日志  记录 端口被占用的信息
                    log.error( "端口:"+ i +"已经被占用");
                }
            }
        }
        System.out.println(  ss.getInetAddress().getHostName() + "启动了，监听了端口号:***"+ss.getLocalPort()+"***");

        //客户端通过键盘录入聊天内容
        Scanner keyBoard = new Scanner(System.in);
        while ( true ) {
            try (
                    Socket s = ss.accept();
                    PrintWriter pw = new PrintWriter(s.getOutputStream()); //println()可以以一行一行行式发送数据
                    Scanner sc = new Scanner(s.getInputStream())      //读取服务器的响应  nextLine()
            ) {
                log.info("客户端:" + s.getRemoteSocketAddress() + "联接上来了");
                do {
                    String response = sc.nextLine();
                    System.out.println("客户端" + s.getRemoteSocketAddress() + "对server说:"+response);
                    if ("bye".equalsIgnoreCase(response)) {
                        System.out.println("客户端" + s.getRemoteSocketAddress() + "主动断开与服务器的联接。。。");
                        break;
                    }
                    System.out.println("请输入服务器对客户端:" + s.getRemoteSocketAddress() + "说的话:");
                    String line = keyBoard.nextLine();
                    pw.println(line);
                    pw.flush();
                    if ("bye".equalsIgnoreCase(line)) {
                        System.out.println("客户端" + s.getRemoteSocketAddress() + "主动断开与服务器的联接。。。");
                        break;
                    }

                } while (true);
                System.out.println("结束聊天" + s.getRemoteSocketAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
