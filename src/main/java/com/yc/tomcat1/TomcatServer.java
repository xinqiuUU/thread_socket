package com.yc.tomcat1;


import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
    服务器
 */
public class TomcatServer {
    //创建日志对象
    static Logger log = Logger.getLogger( TomcatServer.class  );
    public static void main(String[] args) {
        log.debug("服务器启动");
        TomcatServer ts = new TomcatServer();
        int port = ts.parsePortFromXml();
        log.debug("服务器配置端口为:"+ port );
        ts.startServer( port );
    }

    private void startServer( int port ) {
        boolean flag = true;

        try( ServerSocket server = new ServerSocket( port );){
            log.debug( "服务器启动成功，配置端口为:" + port );
            //TODO  可以读取 server.xml 中关于是否开启线程的配置项 决定是否使用线程池
            while (  flag  ){
                try{
                    Socket s = server.accept();
                    log.debug( "客户端:" + s.getRemoteSocketAddress() + "联接上了服务器");
                    TaskService task = new TaskService( s );
                    Thread t = new Thread( task );
                    t.start();
                }catch ( Exception e){
                    e.printStackTrace();
                    log.error( "客户端联接失败。。。");
                }
            }
        }catch ( Exception ex){
            ex.printStackTrace();
            log.error( "服务器套接字创建失败。。。");
        }
    }

    //读取配置文件获取端口
    private int parsePortFromXml(){
        int port = 8090;
        //方案一 : 根据字节码的路径  ( Target/classes/
        //TomcatServer.class.getClassLoader().getResourceAsStream();
        //方案二:
        String serverXml = System.getProperty("user.dir") + File.separator +"conf" +File.separator+ "server.xml";
//        log.info(  serverXml );
        try(
                InputStream iis = new FileInputStream(  serverXml );
            ){
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc  = builder.parse( iis );
            NodeList nl =  doc.getElementsByTagName( "Connector" );
            for (int i=0;i<nl.getLength();i++){
                Element node = (Element) nl.item( i );
                port = Integer.parseInt( node.getAttribute("port") );
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }

}
