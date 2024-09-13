package com.yc.tomcat2;


import com.yc.tomcat2.servlet.YcServletContext;
import com.yc.tomcat2.servlet.YcWebServlet;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

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

        String packageName = "com.yc";
        String packagePath = packageName.replaceAll( "\\.", "/" );
        //服务器启动时  扫描它所有的 classes 查找@YcWebServlet的class，存到map中
        //jvm 类加载器
        try{
            Enumeration<URL> files =  Thread.currentThread().getContextClassLoader().getResources( packagePath );
            while (  files.hasMoreElements() ){
                URL url = files.nextElement();
                log.info( "正在扫描的包路径为:" + url.getFile() );
                //查找此包下的文件
                findPackageClasses( url.getFile(), packageName );
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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

    /*
        扫描包下的所有字节码文件
        把注解和类名 存到 YcServletContext.servletClass 这个map中 键为:/hello  值为:com.yc.tomcat1.servlet.HelloServlet
        packagePath :  包路径  /C:/ideaprojects/gitee_socket/target/classes/com/yc
        packageName : 包名 com.yc
     */
    private void findPackageClasses(String packagePath, String packageName) {
        if (  packagePath.startsWith("/") ){
            packagePath = packagePath.substring( 1 );
        }
        //取这个路径下所有的字节码文件
        File file = new File( packagePath );
        File[] classFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                //过滤调不是.class的文件和不是文件夹    true 的放 classFiles中
                if (pathname.getName().endsWith(".class") || pathname.isDirectory() ) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        if ( classFiles != null && classFiles.length > 0){
            for ( File cf: classFiles){
                if (  cf.isDirectory() ){
                    findPackageClasses( cf.getAbsolutePath() , packageName + "." + cf.getName() ); // com.yc  tomcat1
                }else {
                    //是字节码文件  利用类加载器加载这个  class文件
                    URLClassLoader uc = new URLClassLoader(  new URL[]{}  );
                    try{
                        Class cls = uc.loadClass( packageName + "." + cf.getName().replaceAll( ".class", ""  )  );
//                        log.info( "加载了一个类:" + cls.getName() );
                        if (cls.isAnnotationPresent(  YcWebServlet.class)){
                            log.info( "加载了一个servlet:" + cls.getName() );
//                            String url = ((YcWebServlet)cls.newInstance()).value();
                            YcWebServlet anno = (YcWebServlet) cls.getAnnotation( YcWebServlet.class );
                            String url = anno.value();
                            //通过  注解的value()方法取出  url地址  存到 YcServletContext.servletClass 这个map中
                            YcServletContext.servletClass.put(  url ,  cls );
                            log.info( "servlet的url地址为:" + url +"\t类名为:" + cls.getName() );
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }

//        System.out.println(  classFiles );
    }

    //读取配置文件获取端口
    private int parsePortFromXml(){
        int port = 8090;
        //方案一 : 根据字节码的路径  ( Target/classes/
        //TomcatServer.class.getClassLoader().getResourceAsStream();
        //方案二:
        String serverXml = System.getProperty("user.dir") + File.separator +"conf" +File.separator+ "server.xml";
        log.info(  serverXml );
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
