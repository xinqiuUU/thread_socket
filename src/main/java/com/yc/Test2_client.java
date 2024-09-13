package com.yc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Test2_client {
    public static void main(String[] args) throws IOException {
        InetAddress[] i1 = InetAddress.getAllByName("www.baidu.com");
        if (i1!=null){
            for (InetAddress i:i1){
                Socket client = new Socket(i,80);
                //联接成功:Socket[addr=www.baidu.com/153.3.238.110,port=80,localport=52789]
                //          基于TCP的客服端套接字对象      localport本地端口是随机选的
                System.out.println("联接成功:"+client);
            }
        }
        Socket client2 = new Socket("www.baidu.com",80);
        System.out.println("联接成功2:"+client2);

        Socket client3 = new Socket("www.baidu.com",80);
        System.out.println("联接成功3:"+client3);


    }
}
