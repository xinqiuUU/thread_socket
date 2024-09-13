package com.yc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Test7_telnet_baidu {

    public static void main(String[] args) {
        String website = "www.baidu.com";
        int port = 80;
        //String http = "GET / HTTP/1.0\r\nHOST: www.baidu.com\r\n\r\n";//应用层的协议
        String http = "GET / HTTP/1.0\r\n\r\n";//应用层协议
        // http 服务器 -> http协议
        try(Socket s = new Socket(website,port);
            OutputStream oos = s.getOutputStream();
            InputStream iis = s.getInputStream();
         ){
            oos.write(http.getBytes());
            oos.flush();
            //内存流  防止乱码
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bs = new byte[10*1025];
            int length = -1;
            while ( (length=iis.read(bs,0,bs.length)) != -1 ){
                //先把此次读取到的数据通过baos存到  内存中  防止乱码
                baos.write(  bs , 0 , length );
            }
            baos.flush();

            //一次性读取出来
            byte[] bb = baos.toByteArray();
            String str  = new String(bb);
            System.out.println( str );

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
