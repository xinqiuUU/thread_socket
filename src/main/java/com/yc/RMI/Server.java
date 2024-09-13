package com.yc.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/*
    通过Java RMI提供的一系列底层支持接口，把上面编写的服务以RMI的形式暴露在网络上
 */
public class Server {
    public static void main(String[] args) throws RemoteException {
        System.out.println("创建世界时钟远程服务...");
//        // 设置服务器的实际 IP 地址
//        System.setProperty("java.rmi.server.hostname", "114.55.61.187");
        // 实例化一个WorldClock
        WorldClock worldClock = new WorldClockService();

        // 将此服务转换为远程服务接口。通信端口
        WorldClock skeleton = (WorldClock) UnicastRemoteObject.exportObject(worldClock, 22222);
        // 将RMI服务注册到1099端口:  在本地创建一个RMI注册表实例 并绑定到指定的端口（1099端口）
        Registry registry = LocateRegistry.createRegistry(1099);
        // 注册此服务，服务名为"WorldClock":  远程服务对象 注册到 注册中心
        registry.rebind("WorldClock", worldClock);
        System.out.println("WorldClock 服务器已启动...");
    }
}