package com.yc;

import java.io.*;
import java.net.*;
import java.util.*;

public class JokeServer {
    public static void main(String[] args) throws IOException {
        int port = 10002;
        ServerSocket serverSocket = new ServerSocket(10002);
        System.out.println("JOKE服务器在端口上启动 " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端已连接: " + clientSocket.getInetAddress());

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // 准备笑话列表
            List<String> jokes = Arrays.asList(
                    "猫会喵喵叫，狗会汪汪叫，鸭会嘎嘎叫，鸡会什么，鸡会留给有准备的人。!",
                    "我有一份让人惊讶的工作。“什么工作? 挖藕。",
                    "迪迦是哪里的，东北的，因为我迪迦在东北。",
                    "虾和蚌同时考了一百分，老师问虾:“你抄谁的?”虾说:“我抄蚌的。”老师说:“你棒什么棒!",
                    "如果杀人犯就在门外马上就要夺门而入了怎么办？！把pdd装在门上砍100刀都进不来",
                    "妈妈喜欢打麻将，可是后来我出生了，妈妈为了我也为了整个家庭，毅然决然的放弃了麻将，因为她觉得，好像打我比较有趣。",
                    "愚公移山没有移动他却快死了，临死前他躺在床上对他的子女们说，移山移山，他的子女们都深情的看着他说，亮晶晶～"
            );

            // 随机选择一个笑话并发送给客户端
            Random random = new Random();
            int randomIndex = random.nextInt(jokes.size());
            String randomJoke = jokes.get(randomIndex);
            out.println(randomJoke);

            clientSocket.close();
        }
    }
}

