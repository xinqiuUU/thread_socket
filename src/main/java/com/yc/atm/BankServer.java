package com.yc.atm;

import com.yc.Test3_Server;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

//  服务端
public class BankServer {
    static  final Logger log = Logger.getLogger(Test3_Server.class);

    public static void main(String[] args) throws IOException {
        //CPU的核心数
        int processors = Runtime.getRuntime().availableProcessors();
        //核心线程池的大小
        int corePoolSize = processors;
        //核心线程池的最大线程数
        int maxPoolSize = corePoolSize*2;
        //线程最大空闲时间  即核心线程池中(maxPoolSize-corePoolSize)  以外  的线程空闲存在时间
        long keepAliveTime = 10 ;
        //空闲时间单位
        TimeUnit unit = TimeUnit.SECONDS; // enue枚举  常量
        //阻塞队列  容量为2  最多允许放入两个  空闲任务
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(200);
        // 线程创建工厂
        ThreadFactory threadFactory = new NameThreadFactory();
        //线程池拒绝策略
        RejectedExecutionHandler handler = new MyIgnorePolicy();
        //线程池
        ThreadPoolExecutor executor = null;
        executor = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,unit,workQueue,threadFactory,handler);
        //预启动所有核心线程  提升效率
        executor.prestartAllCoreThreads();

        Bank b = new Bank();
        ServerSocket ss = new ServerSocket(12000);
        log.info("银行服务器启动，监听"+ss.getLocalPort()+"端口");
        boolean flag = true;
        while ( flag ){
            Socket s = ss.accept();//阻塞式 等待客户端的连接
            System.out.println("ATM客户端:"+s.getRemoteSocketAddress()+"登录了服务器");
            //TODO : 商业项目必须用线程池
//            Thread t = new Thread(  new BankTask( s , b )  );
//            t.start();

            BankTask task = new BankTask(  s , b );
            //提交任务到线程池
            executor.submit(task);

        }
        executor.shutdown();
    }
    //线程工厂
    static class NameThreadFactory implements ThreadFactory{
        // 线程id  AtomicInteger  原子类
        //原子性  可见性   有序性
        private final AtomicInteger threadId = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r,"线程-"+threadId.getAndIncrement());
//            System.out.println(t.getName()+"已经被创建");
            return t;
        }
    }
    /*
            线程池的拒绝策略
         */
    static class MyIgnorePolicy implements RejectedExecutionHandler{
        private Logger log = Logger.getLogger( MyIgnorePolicy.class.getName() );
        //  Runnable :被拒绝的任务  ThreadPoolExecutor：线程池对象
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            doLog(r,executor);
        }
        private void doLog(Runnable r, ThreadPoolExecutor executor){
//            System.out.println("线程池:"+executor.toString()+r.toString()+"被拒绝执行");
                log.info("线程池:"+ executor.toString()+r.toString()+"被拒绝执行");
        }
    }
}
