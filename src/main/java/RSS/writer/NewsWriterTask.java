package RSS.writer;

import RSS.Buffer.NewsBuffer;
import RSS.bean.CommonInformationItem;
import RSS.dao.ProjectProperties;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

//从缓存中取数据   写到磁盘 (mongoDB 、 数据库 )
public class NewsWriterTask implements Runnable{

    private Logger log = Logger.getLogger(NewsWriterTask.class.getName());
    private  String name; // RSS源的名称  用来做磁盘文件的文件名
    private NewsBuffer buffer; // 缓存

    private boolean flag = false;

    public NewsWriterTask(NewsBuffer buffer){
        this.buffer = buffer;
        this.name = "未知新闻源";
    }
    public NewsWriterTask(String name, NewsBuffer buffer){
        this.name = name;
        this.buffer = buffer;
    }
    @Override
    public void run() {
        //循环从buffer中取新闻 ， 存到磁盘 (mongoDB 、 数据库)
        while ( !flag && !Thread.currentThread().isInterrupted()){
            CommonInformationItem item = null;
            try {
                item = buffer.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if ( item == null){
                continue;
            }
            //将此item写到磁盘
            Path p = Paths.get( ProjectProperties.getInstance().getProperty("file.path") + item.getFileName() );

            //流:                    创建一个缓冲字符流(因为可以一行一行的写数据 ) CREATE: 每次都新建文件
            try(BufferedWriter writer = Files.newBufferedWriter( p , StandardOpenOption.CREATE)){
                writer.write(   item.toString()   );
                writer.flush();
            }catch (Exception e){
                e.printStackTrace();
                log.error("保存信息失败:"+e);
            }
        }

    }
}
