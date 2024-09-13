
RSS订阅程序
    使用   Scheduled     ThreadPoolExecutor完成周期性(每隔一分钟)的读取RSS订阅内容.

名词解释:
    RSS（Really Simple Syndication）是一种用于发布经常更新的信息（如博客文章、新闻头条、音频和视频）的Web内容分发格式。
    通过RSS订阅，你可以在一个地方集中获取来自多个网站的最新内容，而无需逐一访问这些网站

RSS协议格式：

分析:
   1. 对每一个RSS源, 向执行器发送一个Runnable对象。在这里面解析RSS源，并且将其转换为一个含有RSS内容的 CommonInformationItem 对象
   2. 使用生产者/消费者模式将RSS新闻写入磁盘。
      生产者  =>  缓存(先进行出=>队列 +阻塞 有界 + 去重 ).　　　=>消费者从缓存中读取新闻并将其写入磁盘.

         生产者是执行器的任务，它将CommonInformationItem写入到缓存中。   消费者是一个独立线程，它从缓存中读取新闻并将其写入磁盘.
   3. 保证不读取重复新闻.
   4. RSS文件就是一个XML， 所以要用到xml解析技术.
        原生:  DOM解析:  文档对象模型的解析方式　　->需要一次性将全部xml文件加载到内存
              SAX解析:  基于事件的解析方式.   ->>  速度快.  ->边下载边解析.      vvvvvv
        框架: JDOM框架
              Dom4J框架

技术点:
　 1. 生产者               /                             消费者模式:
              引入了缓存NewsBuffer充当缓存队列
  2. xml解析技术: SAX基于事件的xml解析.
  3. 执行周期性任务的线程池   ScheduledThreadPoolExecutor类
  4. 去重.

版本:
1. 基础版: 使用标准的 ScheduledThreadPoolExecutor类执行周期性任务. 对每个RSS源执行一个任务. 间隔时间为1分钟.

步骤:
1. 先开发公共组件:
　　　CommonInformationItem类
     RSSDataCapturer类: 利用SAX完成RSS源的解析

     完成测试
2. 开发缓存，这是一个生产者/消费者模式的缓存队列
   功能:   1。 缓存新闻对象到内存   2. 从缓存中取新闻对象.

    完成测试.
3. 开发消费端:   从缓存中取新闻对象写到磁盘上. 这是一个任务   NewsWriter类.

    完成测试.
4. 开发 生产任务.   NewsSystem及NewsTask类

5.  主程序 :  Main类.