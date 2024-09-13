package com.yc;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Test5_talkServer_runnable {
    public static void main(String[] args) {
        ServerSocket ss=null;
        try {
            ss= new ServerSocket(10003);
        } catch (IOException e) {
            if (e instanceof BindException){
                System.out.println("端口号被占用！");
            }
            e.printStackTrace();
        }
        System.out.println(ss.getInetAddress().getHostName() + "启动了,监听端口: *****" + ss.getLocalPort() + "***...");

        while( true ){
            try{
                Socket s=ss.accept();
                System.out.println("客户端:" + s.getRemoteSocketAddress() + "连接上来了");
                TalkTask tt= new TalkTask( s );
                Thread t=new Thread( tt );
                t.start();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}

class TalkTask implements Runnable {
    private Socket s;
    Scanner keyboard=new Scanner(System.in);

    public TalkTask( Socket s ){
        this.s=s;
    }

    @Override
    public void run() {
        try{
            Scanner clientReader = new Scanner(s.getInputStream());
            PrintWriter pw = new PrintWriter(s.getOutputStream());

            do {
                String response = clientReader.nextLine();
                System.out.println("客户端说:" + s.getRemoteSocketAddress() + " #server说:" + response);
                if ("bye".equalsIgnoreCase(response)) {
                    System.out.println("客户端:" + s.getRemoteSocketAddress() + "结束连接...");
                    break;
                }

                System.out.println("请输入回复的内容:");
                String line = keyboard.nextLine();
                pw.println(line);
                pw.flush();
                if ("bye".equalsIgnoreCase(line)) {
                    System.out.println("服务端即将关闭与客户端:" + s.getRemoteSocketAddress() + "的连接...");
                    break;
                }
            } while (true);

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
