package com.yc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/*
    因为直接用socket开发客户端比较麻烦  =》 jdk 提供了更高级的工具类
 */
public class Test8_url_urlconnection_httppurlconnection {
    private static Logger log = Logger.getLogger( Test8_url_urlconnection_httppurlconnection.class.getName() );

    public static void main(String[] args) throws IOException {
        //1.URL类 URL : 统一资源定位符  + URLConnection
        URL url = new URL("http://www.baidu.com");
        URLConnection con = url.openConnection();;

        String contentType = con.getContentType();;
        long conlength = con.getContentLength();
        log.info(  contentType + "\t" + conlength);

        //响应体头没有  响应体头的内容已经被解析变成了 con中的属性值
        InputStream iis = con.getInputStream();

        byte[] bs = new byte[10*1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int length = -1;
        while (  (length = iis.read(bs,0,bs.length)) != -1 ){
            baos.write(bs,0,length);
        }
        baos.flush();

        byte[] bb = baos.toByteArray();
        String str = new String(bb);
        System.out.println(str); // 响应头  响应实体( html )



    }

}
