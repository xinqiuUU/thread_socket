package com.yc.RMI;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.rmi.server.RemoteServer.getClientHost;


public class WorldClockService implements WorldClock {
    @Override
    public LocalDateTime getLocalDateTime(String zoneId) throws RemoteException, ServerNotActiveException {
        // 获取客户端 IP 地址
        String clientIp = getClientHost();
        System.out.println("客户端 IP: " + clientIp);
        //根据时区获得当地时间并把纳秒部分设置为0
        return LocalDateTime.now(ZoneId.of(zoneId)).withNano(0);
    }
}