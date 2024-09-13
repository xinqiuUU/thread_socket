package com.yc;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;

/*
     客 服  端
 */
public class Test3_Server {

    public static void main(String[] args) throws IOException, InterruptedException {
        final Logger log = Logger.getLogger(Test3_Server.class);
//        final int port = 10000;
        ServerSocket ss = null;
//        for ( int i=10000;i<=65535;i++){
//            try{
//                //选择合适端口的工作
//                ss = new ServerSocket( i );
//                break;
//            }catch (Exception e){
////                e.printStackTrace();
//                if (e instanceof BindException){
//                    //用错误日志  记录 端口被占用的信息
//                    log.error( "端口:"+ i +"已经被占用");
//                }
//            }
//        }
        ss = new ServerSocket( 10002 );
        //用 info 级别记录  ServerSocket套接字创建成功的信息
        log.info( ss.getInetAddress().getHostAddress()+"启动了，监听了端口号:***"+ss.getLocalPort()+"***...");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date d = null;
//        String time = null;
        while(true){
            log.info("服务器ServerSocket准备等待客户端的连接");
            Socket socket = ss.accept();//阻塞式方法
            log.info("获取了一个与客户端:"+socket.getRemoteSocketAddress() +"的联接");

            //TODO  服务器中的业务
//            d = new Date();
        //        time = df.format(d);
//            time = "好好好";
//                try(OutputStream oos = socket.getOutputStream();
//                    PrintWriter out = new PrintWriter(new OutputStreamWriter(oos, "UTF-8"), true)) {
//                    out.println(time);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            out.println(time);

            TextRunnable textRunnable = new TextRunnable(socket);
            Thread thread = new Thread(textRunnable);
            thread.start();

            thread.join();

            socket.close();
            log.info("服务器断开与客户端的联接");
            System.out.println("服务器断开与客户端的联接");
        }
    }


    static class TextRunnable implements Runnable{
        private Socket socket;

        public TextRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter( socket.getOutputStream() );
                String message;
                List<String> lines = new ArrayList<>();
                while (true) {
                    message = reader.readLine();
                    if (message == null || message.isEmpty()) {
                        Thread.sleep(1000); // 如果消息为空，线程休眠1秒钟
                    } else {
                        // 收到有效消息，发送响应
                        System.out.println("收到客户端消息：" + message);
                        //Test3_Server.class.getResourceAsStream("/text/" + message + ".txt")
//                        Test3_Server.class.getResourceAsStream("/" + fileName)
//                        BufferedReader br = new BufferedReader(new FileReader("C:\\ideaprojects\\gitee_socket\\src\\main\\java\\com\\yc\\text\\"+message+".txt"));
                        BufferedReader br;
                        if("1".equals(message) || "2".equals(message) || "3".equals(message) ){
                            br = new BufferedReader(new InputStreamReader(
                                    Test3_Server.class.getResourceAsStream("/text/" + message + ".txt"))) ;
                        }else {
                            br = new BufferedReader(new InputStreamReader(
                                    Test3_Server.class.getResourceAsStream("/text/1.txt"))) ;
                        }
                        String line;
                        while ((line = br.readLine()) != null) {
                            lines.add(line);
//                            System.out.println(line);
                        }
                        out.println("服务器收到消息:" + lines);
                        out.flush();  // 确保消息发送到客户端
                        break; // 处理完消息后退出循环
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

