package RSS.Buffer;

import RSS.bean.CommonInformationItem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/*
    爬取的新闻的  缓存
 */
public class NewsBuffer {
    // 用于存新闻的  阻塞  队列
    private LinkedBlockingQueue<CommonInformationItem> buffer;
    //用于去重的一个map 线程安全的  哈希表
    private ConcurrentHashMap<String, String> storeItem; // 已经保存的新闻的id

    public NewsBuffer(){
        buffer = new LinkedBlockingQueue<>();
        storeItem = new ConcurrentHashMap<>();
    }
    public void add(CommonInformationItem item){
        //去重
        if ( storeItem.containsKey(item.getId())){
            return;
        }
        buffer.add(item);
        storeItem.put( item.getId() ,  item.getId() );
        //TODO 大数据保存方式 ?  hash算法  -> 位图  ->布隆过滤器(redis实现)

    }
    public CommonInformationItem get() throws InterruptedException {
        //从阻塞队列中获取一个item
        return buffer.take();
    }

}
