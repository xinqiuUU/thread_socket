package RSS.Buffer;

import RSS.bean.CommonInformationItem;
import RSS.bean.RSSDataCapturer;
import junit.framework.TestCase;

import java.util.List;

/*
    爬取的新闻的缓存
     测试类 逻辑 :
        1.  先执行setUp() 方法
        2.  执行testXxx() 方法

 */
public class NewsBufferTest extends TestCase {
    private List<CommonInformationItem> list ;
    private NewsBuffer buffer = new NewsBuffer();

    /*
        在下面的testXxx()  方法之前一定执行的提前操作
     */
    public void setUp() throws Exception{
        System.out.println( "setUp" );
        RSSDataCapturer rdc = new RSSDataCapturer("爱范儿RSS");
        String path = "https://www.ifanr.com/feed";
        list = rdc.load( path );
    }

    public void testAdd() {
        for (CommonInformationItem item : list){
            buffer.add(  item );
        }
        assertNotNull( buffer ); //我断言buffer缓存非空
    }
    public void testGet() throws InterruptedException {
        CommonInformationItem item = buffer.get();
        assertNotNull(  item ); //我断言获取的item非空
    }

}