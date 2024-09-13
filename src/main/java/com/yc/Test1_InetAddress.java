package com.yc;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test1_InetAddress {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress i1 = InetAddress.getLocalHost();
        System.out.println(i1);
        InetAddress[] i2 = InetAddress.getAllByName("www.baidu.com");
        if (  i2!= null ){
            for (InetAddress i:i2){
                System.out.println(i);
            }
        }
    }
}
