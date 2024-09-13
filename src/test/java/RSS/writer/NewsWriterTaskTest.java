package RSS.writer;

import RSS.Buffer.NewsBuffer;
import RSS.bean.CommonInformationItem;
import RSS.bean.RSSDataCapturer;
import junit.framework.TestCase;

import java.util.List;

public class NewsWriterTaskTest extends TestCase {

    private List<CommonInformationItem> list ;
    private NewsBuffer buffer = new NewsBuffer();


    public void setUp() throws InterruptedException {
        System.out.println( "setUp" );
        RSSDataCapturer rdc = new RSSDataCapturer("摄影世界");
        String path = "https://www.ifanr.com/feed";
        list = rdc.load( path );
        for (CommonInformationItem item : list){
            buffer.add(  item );
        }
        NewsWriterTask task = new NewsWriterTask("摄影世界", buffer);
        Thread  thread = new Thread( task );
        thread.start();
        thread.join();
    }

    public void testT() {

    }


}