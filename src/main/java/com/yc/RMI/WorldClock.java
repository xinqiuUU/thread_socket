package com.yc.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.time.LocalDateTime;

//要实现RMI，服务器和客户端必须共享同一个接口
public interface WorldClock extends Remote {
    LocalDateTime getLocalDateTime(String zoneId) throws RemoteException, ServerNotActiveException;
}