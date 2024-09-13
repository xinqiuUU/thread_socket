package xunlei;

import org.apache.log4j.Logger;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

/*
    迅雷下载 的 任务类
 */
public class DownLoadTask implements Runnable{
    private int i;
    private long fileSize;
    private int threadSize;
    private long sizePerThread;
    private String url;
    private String downloadPath;
    private static final Logger log = Logger.getLogger( DownLoadTask.class.getName());

    //回调函数
    private Callback callback;
    private AtomicLong dlSize;

    //第二种 方法  回调函数
    public DownLoadTask(int i, long fileSize, int threadSize, long sizePerThread, String url, String downloadPath,Callback callback) {
        this.i = i;
        this.fileSize = fileSize;
        this.threadSize = threadSize;
        this.sizePerThread = sizePerThread;
        this.url = url;
        this.downloadPath = downloadPath;
        this.callback = callback;
    }
    //第一种 方法  传引用过来
    public DownLoadTask(int i, long fileSize, int threadSize, long sizePerThread, String url, String downloadPath, AtomicLong dlSize) {
        this.i = i;
        this.fileSize = fileSize;
        this.threadSize = threadSize;
        this.sizePerThread = sizePerThread;
        this.url = url;
        this.downloadPath = downloadPath;

        this.dlSize = dlSize;
    }

    @Override
    public void run() {
        //计算此线程要下载的起始和终止位置
        long start = i * sizePerThread;
        long end = ( i + 1 )*sizePerThread-1;//最后一个线程要下载的终止位置
        RandomAccessFile raf = null;
        BufferedInputStream bis = null;
        try{
            //让RandomAccessFile 的指针偏移到这个位置
            raf = new RandomAccessFile( downloadPath , "rw");
            //将文件指针移动到  start 位置 从这个位置开始写入
            raf.seek( start );
            //3.开始下载  Range请求头
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range","bytes=" + start + "-" + end );
            connection.setConnectTimeout(3000);
            connection.connect();

            bis = new BufferedInputStream(connection.getInputStream());
            byte[] bs = new byte[10 * 1024];//每次读10k
            int length = -1;
            while( (length = bis.read(bs,0,bs.length) ) != -1){
                raf.write( bs , 0 , length );
//                log.info(Thread.currentThread().getName()+"\t下载了"+length+"字节");
                //*****第一种方法  传引用过来 *****
//                if (  dlSize != null){
//                    dlSize.addAndGet(length);
//                }
//                System.out.print("\r总大小:"+Xunlei.dlSize);
//                // 计算百分比
//                double percentage = ((double) dlSize.get() / fileSize) * 100;
//                // 打印结果
//                System.out.printf("\t\t下载进度: %.2f%%", percentage);

                //*****第二种方法  使用回调函数  *******
                // 每读取10k调用一次，调用回调函数通知主线程
                callback.onCallback(length);

            }
            //log.info("下载完毕:");
            // System.out.println("总大小:"+Xunlei.dlSize);
        }catch (Exception e){
            e.printStackTrace();
            log.error("下载异常"+e);
        }finally {
            if (bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (raf != null){
                try {
                    raf.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }
}
