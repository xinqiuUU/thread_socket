package com.yc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/*
    基于UDP的Socket的发送端
 */
public class Test9_udp_sender {
    public static void main(String[] args) throws IOException {
        String s = "中文";
        DatagramPacket dp = new DatagramPacket(s.getBytes(),s.getBytes().length,new InetSocketAddress("127.0.0.1",3333));
        DatagramSocket ds = new DatagramSocket(15678);
        ds.send(dp);
        ds.close();
        System.out.println("发送成功");
    }
}
