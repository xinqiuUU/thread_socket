package RSS.bean;

import junit.framework.TestCase;

import java.util.List;

/*
     单元测试
 */
public class RSSDataCapturerTest extends TestCase {


    public void testLoad() {
        //String path = System.getProperty("user.dir")+ File.separator+"rss.xml";
        String path = "https://www.ifanr.com/feed";
        RSSDataCapturer rdc = new RSSDataCapturer("爱范儿RSS");
        List<CommonInformationItem> list = rdc.load( path );
        for (CommonInformationItem item : list){
            System.out.println( item );
        }
        System.out.println( "总共得到:" + list.size() + "条新闻");
    }


}