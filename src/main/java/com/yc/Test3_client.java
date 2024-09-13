package com.yc;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Test3_client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",10002);
        System.out.println("联接成功:"+socket);

        try( InputStream iis = socket.getInputStream() ){
            byte[] bs = new byte[1024]; //1k
            int length = -1;
            System.out.println("客服端等待服务器的响应");
            while( (length=iis.read(bs,0,bs.length)) !=-1 ){
                String str = new String( bs,0,length);
                System.out.println("服务器的响应:"+str);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("客服端断开与服务器的连接");
        socket.close();
    }
}
