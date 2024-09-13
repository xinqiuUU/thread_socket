package com.yc.atm;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//  任务类
public class BankTask implements Runnable{
    private Socket socket;
    private Bank b;
    private boolean flag;

    public BankTask(Socket socket, Bank b) {
        this.socket = socket;
        this.b = b;
        this.flag=true;
    }

    // 任务 类
    @Override
    public void run() {
        try( //try-with-resources // 会自动关闭 释放资源
               Socket s = this.socket;
               Scanner reader = new Scanner( s.getInputStream() );
               PrintWriter pw = new PrintWriter( s.getOutputStream() );
                ){
            while (flag && !Thread.currentThread().isInterrupted()){
                //客户端是否没有数据  如没有传 则结束
                if (  !reader.hasNext() ){
                    System.out.println("ATM客户端:"+s.getRemoteSocketAddress()+"掉线了");
                    break;
                }
                //如果由信息 则取出
                String command = reader.next(); //next()读第一个部分 而没有读取一行
                BankAccount ba = null;

                JsonModel<BankAccount> jm = new JsonModel<>();
                //命令模式:
                if ("DEPOSIT".equals(command)){
                    int id = reader.nextInt();
                    double money = reader.nextDouble();
                    ba = b.deposite(id,money);
                }else if ("WITHDRAW".equals(command)){
                    int id = reader.nextInt();
                    double money = reader.nextDouble();
                    ba = b.withdraw(id,money);
                }else if ("BALANCE".equals(command)){
                    int id = reader.nextInt();
                    ba = b.search(id);
                }else if ("QUIT".equals(command)){
                    System.out.println("ATM客户端要求断开。。。"+s.getRemoteSocketAddress());
                    break;
                }else {
                    jm.setCode(0);
                    jm.setError("错误命令");

                    Gson g = new Gson();
                    String jsonString = g.toJson( jm );
                    pw.println(  jsonString );
                    pw.flush();
                    continue;
                }
                jm.setCode(1);
                jm.setObj(ba);

                Gson g = new Gson();
                String jsonString = g.toJson( jm );
                pw.println(  jsonString );
                pw.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
