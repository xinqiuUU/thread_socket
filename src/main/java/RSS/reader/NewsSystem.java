package RSS.reader;

import RSS.Buffer.NewsBuffer;
import RSS.writer.NewsWriterTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/*
    定时调度 类
 */
public class NewsSystem implements Runnable{
    private String sourcePath ;  //source.txt 路径
    private ScheduledThreadPoolExecutor executor; // 定时调度线程池

    private NewsBuffer newsBuffer;

    public NewsSystem(String sourcePath){
        this.sourcePath = sourcePath;
        this.executor = new ScheduledThreadPoolExecutor( Runtime.getRuntime().availableProcessors()  );
        newsBuffer = new NewsBuffer();
    }


    @Override
    public void run() {
        //消费任务
        NewsWriterTask writerTask = new NewsWriterTask( newsBuffer );
        Thread t = new Thread(  writerTask  );
        t.start();
        //读取 source.txt中的RSS源的地址  有几个源 就启动几个生产任务
        Path file = Paths.get(  sourcePath  );
        try(BufferedReader reader = new BufferedReader(  new FileReader( file.toFile() )) ){
            String line = null;
            //每个 RSS源 启动一个生产者线程池
            while (  (line= reader.readLine()) != null){
                String[] strs = line.split(  ";"  );
                if ( strs!=null && strs.length == 2){
                    String rssName = strs[0];
                    String rssUrl = strs[1];
                    //创建生产者的任务
                    NewsProductTask productTask = new NewsProductTask( rssName, rssUrl, newsBuffer );
                    // 线程池  每隔 10 分钟执行一次
                    executor.scheduleWithFixedDelay( productTask, 0, 10, TimeUnit.MINUTES);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
