package xunlei;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/*
    迅雷下载
 */
public class Xunlei {

    //volatile -> 修饰属性 保证变量的可见性和有序性，但它 ***  不保证原子性  ***
    // AtomicInteger -> 保证原子性  ：  线程安全
    public static AtomicLong dlSize = new AtomicLong(0);  //回调
//    public static Long dlSize = new Long(0);  //回调

    // 日志  信息
    private static Logger log = Logger.getLogger( Xunlei.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        //抖音下载网站
        String url = "https://dldir1.qq.com/qqfile/qq/QQNT/Windows/QQ_9.9.15_240902_x64_01.exe";
        //1.获取要下载的文件的大小
        long fileSize = getDownLoadFileSize( url );
        log.info("待下载的文件大小:"+fileSize);
        //2.新文件保存的位置
        //新文件名
        String newFileName = genFileName( url );
        log.info("新文件保存的文件名:"+newFileName);
        String userhome = System.getProperty("user.home");//C:\Users\DELL
//        System.out.println(userhome);
        //File.separator 根据当前操作系统自动获取的文件路径分隔符 \
        String downloadPath = userhome + File.separator + newFileName;
        log.info("下载后新文件的路径:" + downloadPath );
        //3.创建此新文件( 空 )  "rw" 表示可读可写  创建一个可读写的随机访问文件
        RandomAccessFile raf = new RandomAccessFile( downloadPath , "rw" );
        raf.setLength(  fileSize  );
        raf.close();
        log.info("空的新文件创建成功。。。。");
        //4.获取本机的核数=》线程数
        int threadSize = Runtime.getRuntime().availableProcessors();
        //5.计算每个线程要下载的字节数
        long sizePerThread = getSizePerThread( fileSize , threadSize );
        //6.循环创建线程，每个线程下载自己的部分
        log.info("每个线程要下载的数据量为:" + sizePerThread );

        List<Thread> threads = new ArrayList<>();
        for ( int i=0 ; i<threadSize;i++){
            //*****第二种 方法  回调函数 *****
            //创建 回调  匿名类实例  记录当前下载进度
            Callback callback = new Callback() {
                @Override
                public void onCallback(int length) {
                    //不加锁
                    if (dlSize != null) {
                        //当前 值 加 length 返回新值
                        dlSize.addAndGet(length);
                        System.out.print("\r文件大小:"+dlSize);
                        // 计算百分比
                        double percentage = ((double) dlSize.get() / fileSize) * 100;
                        // 打印结果
                        System.out.printf("\t\t下载进度: %.2f%%", percentage);
                    }
                    //加锁
//                    synchronized ( Xunlei.class){
//                            if (dlSize != null) {
//    //                        //当前 值 加 length 返回新值
//                            dlSize += length;
//                            System.out.print("\r文件大小:"+dlSize);
//                            // 计算百分比
//                            double percentage = ((double) dlSize / fileSize) * 100;
//                            // 打印结果
//                            System.out.printf("\t\t下载进度: %.2f%%", percentage);
//                        }
//                    }
                }
            };
            DownLoadTask task = new DownLoadTask(  i,  fileSize, threadSize, sizePerThread, url, downloadPath,callback );
            //***** 第一种 方法  传引用过去 *****
//            DownLoadTask task = new DownLoadTask(  i,  fileSize, threadSize, sizePerThread, url, downloadPath,dlSize );
            Thread thread = new Thread(  task  );
            thread.start();
            threads.add(thread);
        }
        long start = System.currentTimeMillis();
//        System.out.println("开始时间:"+start);
        // 等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("\t下载用时:"+(end-start));
    }

    //计算每个线程要下载的字节数
    private static long getSizePerThread(long fileSize, int threadSize) {
        return fileSize%threadSize==0?fileSize/threadSize:fileSize/threadSize+1;
    }

    // 根据日期和时间生产文件的文件名
    private static String genFileName(String url){
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String prefix = df.format( date );
        //后缀名
        String suffix = url.substring( url.lastIndexOf("."));
        return  prefix+suffix;
    }

    //正式下载前  获取文件大小  "HEAD"
    private static long getDownLoadFileSize(String url) throws IOException {
        long fileSize = 0;
        // 根据传入的 URL 字符串创建一个 URL 对象
        URL u = new URL( url );
        // 打开与该 URL 的连接，并将其强制转换为 HttpURLConnection 类型
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        //设置请求头
        connection.setRequestMethod("HEAD"); // head不下载文件内容  取 响应头域( 文件大小 )
        // 设置连接超时时间为 3000 毫秒
        connection.setConnectTimeout( 3000 );
        // 建立连接
        connection.connect();
        // 通过 HttpURLConnection 获取文件的内容长度，并将其赋值给 fileSize
        fileSize = connection.getContentLength();
        log.info( "文件大小:"+fileSize);
        return fileSize;
    }
}
