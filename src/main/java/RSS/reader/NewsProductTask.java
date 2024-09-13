package RSS.reader;

import RSS.Buffer.NewsBuffer;
import RSS.bean.CommonInformationItem;
import RSS.bean.RSSDataCapturer;
import org.apache.log4j.Logger;

import java.util.List;

/*
    生产者任务 类
 */
public class NewsProductTask implements Runnable{

    private Logger log = Logger.getLogger(NewsProductTask.class.getName());
    private String name;
    private String url;
    private NewsBuffer newsBuffer;

    public NewsProductTask(String name, String url, NewsBuffer newsBuffer) {
        this.name = name;
        this.url = url;
        this.newsBuffer = newsBuffer;
    }

    @Override
    public void run() {
        //解析 source对应的RSS源 得到item集合  返回
        RSSDataCapturer rdc = new RSSDataCapturer(  name  );
        List<CommonInformationItem> list =  rdc.load( url );
        //存到缓存队列中
        for ( CommonInformationItem item : list){
            newsBuffer.add( item );
        }
        log.info("下载了:"+name+"源的"+url+"的新闻成功");
    }
}
