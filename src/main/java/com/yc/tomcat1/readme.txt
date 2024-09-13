版本： http服务器，提供静态资源访问
请提交： http://localhost:8090/wowotuan/index.html 显示wowotuan页面。

分析情况：
请求部分：（浏览器自动实现）
    GET /wowotuan/index.html HTTP/1.1
    Referer: xxxx
    User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.16
    ...
    空行

服务器响应部分：
    HTTP/1.1 200 OK
    Accept-Ranges: bytes
    Content-Length: 92174
    Content-Type: text/html
    ...
    空行
    响应实体（index.html的文本内容）

服务器功能：
    1. 接收各个端的请求解析出它们请求的文件名（/wowotuan/index.html）及相对路径（d:\IdeaProjects\yc119_net\wepapps + /wowotuan/index.html)
        System.getProperty("user.dir")
    2. 查找这个文件是否存在， 不存在 ->  拒绝404 页面
                            存在 -> 1) 提取这个资源，文件输入流。
                                    2) 构建响应报文
                                    HTTP/1.1 200 OK
                                    Content-Type:浏览器响应中的Content-Type来决定使用什么引擎解析解析数据
                                               text/html ： html -> html渲染
                                               text/css   :css引擎
                                               text/javascript: js引擎
                                               image/jpeg: 图片引擎
                                    Content-Length:
    用到的技术:
        1.ServerSocket -> Socket
        2.多线程
        3.log4j
        4. 属性的处理: 因为字节码无法修改 -> 属性文件
                        1)properties: Properties类  .load()
                        2)xml文件   ->  server.xml
                        3)json文件  ->  以流的方式读取 -> String ->Gson.toJsonObject()..转为json对象
          xml解析:
              a)DOM方式: javascript:document.getElementsByTagName("xxx")
                        特点:一次性将整个xml文件加载到内存中
              b)SAX方式: 事件解析方式
          以上两种解析方式  j2EE支持
           xml解析框架：
              c)JDOM
              d)dom4j