package com.yc;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/*
    单 聊
 */
public class Test_talkClient {
    static  final Logger log = Logger.getLogger(Test_talkClient.class);

    public static void main(String[] args) throws IOException {
        InetAddress ia = InetAddress.getByName( "localhost");
        System.out.println(ia);
        Socket s = new Socket( ia,10003);
        log.info("客户端联接服务器"+s.getRemoteSocketAddress() + "成功");
        //客户端 通过 键盘
        try(Scanner keyboard = new Scanner(System.in); //标准输入流 -》键盘
            //套接字流
            Scanner sc = new Scanner(s.getInputStream());
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            ){
            do {
                System.out.println("请输入客户端对服务器说的话:");
                String line = keyboard.nextLine();
                //套接字提供的输出流
                pw.println(  line  );
                pw.flush();

                if ("bye".equalsIgnoreCase(line)) {
                    System.out.println("客户端" + s.getRemoteSocketAddress() + "主动断开与服务器的联接。。。");
                    break;
                }
                String response = sc.nextLine();
                System.out.println("服务器对客户端说:" + response );
                if ("bye".equalsIgnoreCase(response)) {
                    System.out.println("客户端" + s.getRemoteSocketAddress() + "主动断开与服务器的联接。。。");
                    break;
                }

            }while (true);
            System.out.println("在客户端正常结束聊天。。。");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("客户端关闭。。");
    }

}
