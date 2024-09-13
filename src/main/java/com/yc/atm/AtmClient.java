package com.yc.atm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yc.Test3_Server;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;

/*
    客户 端
 */
public class AtmClient {
    static  final Logger log = Logger.getLogger(AtmClient.class);

    public static void main(String[] args) {
        String host = "localhost";
        int port = 12000;
        //创建一个socket
        Scanner keyBoard = new Scanner(System.in);//键盘
        boolean flag = true;

        try(
                Socket s = new Socket(host,port);
                PrintWriter pw = new PrintWriter(s.getOutputStream()); //println()可以以一行一行行式发送数据
                Scanner sc = new Scanner(s.getInputStream())      //读取服务器的响应  nextLine()
                ){
            log.info("联接ATM服务器"+s.getRemoteSocketAddress()+"成功");

            do {
                System.out.println("======ATM=======");
                System.out.println("1.存");
                System.out.println("2.取");
                System.out.println("3.查询");
                System.out.println("4.退出");
                System.out.println("5.错误命令");
                System.out.println("===============");
                System.out.println("请输入你的选项");
                String command = keyBoard.nextLine();
                String response = null;
                if ("1".equalsIgnoreCase(command)){
                    pw.println("DEPOSIT 1 100");
                }else if ("2".equalsIgnoreCase(command)){
                    pw.println("WITHDRAW 1 10");
                }else if ("3".equalsIgnoreCase(command)){
                    pw.println("BALANCE 1");
                }else if ("5".equalsIgnoreCase(command)){
                    pw.println("adfdsf");
                }else {
                    pw.println("QUIT");
                    pw.flush();
                    flag = false;
                    break;
                }
                pw.flush();
                //取服务器的响应
                response = sc.nextLine();
                System.out.println("服务器的响应:"+response);
                Gson gson = new Gson();
                Type type = new TypeToken< JsonModel<BankAccount> >(){}.getType();
                JsonModel<BankAccount> jm = gson.fromJson( response , type );
                if ( jm.getCode()==1){
                    BankAccount ba = jm.getObj();
                    System.out.println(ba.getId()+"\t"+ba.getBalance());
                }else {
                    System.out.println( jm.getError() );
                }
            }while (flag);

            System.out.println("ATM机退出");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
