package com.yc;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Test9_udp_receiver {

    public static void main(String[] args) throws IOException {
        byte buf[] = new byte[3];
        DatagramPacket dp = new DatagramPacket(buf,buf.length);
        DatagramSocket ds = new DatagramSocket(3333);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true){
            System.out.println("阻塞式的 receive方法");
            ds.receive(dp);
            System.out.println(new String(buf,0, dp.getLength()));
        }
    }
}
