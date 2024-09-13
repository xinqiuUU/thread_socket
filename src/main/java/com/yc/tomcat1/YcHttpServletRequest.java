package com.yc.tomcat1;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/*
    从输入流中读取http请求信息  解析出相应的信息  存好
 */
public class YcHttpServletRequest {
    private static Logger log = Logger.getLogger( YcHttpServletRequest.class );

    private Socket s ;
    private InputStream iis;
    //GET post  put delete options  head trace options
    private String method;

    /*
        定位符 http://localhost:81/res/doUpload.action
     */
    private String requestURL;
    //标识符 /res/doUpload.action
    private String requestURI;
    //上下文  /res
    private String contentPath;
    //请求字符串  请求的地址栏参数   age=12&name=zhangsan
    private String queryString;

    // 参数 ： 地址栏参数  uname=abc&age=12  表单中的参数->请求实体 ： sex=男&ins=打开、游戏
    private Map<String,String[]> parameterMap = new ConcurrentHashMap<>();
    //协议类型: http://
    private String scheme;
    //协议版本: HTTP/1.1
    private String protocol;
    //项目 的 真实路径
    private String realPath;


    public YcHttpServletRequest(Socket s ,  InputStream iis ) {
        this.s=s;
        this.iis = iis;
        this.parseRequest();
    }
    /*
        TODO :  解析方法
     */
    private void parseRequest(){
        String requestInfoString = readFromInputStream(); // 从输入流中读取http请求信息( 文字 )
        if (  requestInfoString == null || "".equals(requestInfoString.trim())){
            throw new RuntimeException( "读取输入流异常" );
        }
        //2. 解析 http请求头 (存各种信息)
        parseRequestInfoString( requestInfoString );
    }

    // 解析 http请求头 (存各种信息)
    //"GET /index.html HTTP/1.1\r\nHost: example.com\r\nUser-Agent: Mozilla/5.0\r\n\r\n"
    private void parseRequestInfoString(String requestInfoString) {
        StringTokenizer st = new StringTokenizer( requestInfoString ); //按  空格  切割
        this.method =  st.nextToken();//"GET"
        this.requestURI = st.nextToken();//"/index.html?age=12&name=zhangsan HTTP/1.1"
        int questionIndex = this.requestURI.lastIndexOf( "?" );
        if ( questionIndex >=0 ){
            //有? 则有地址栏参数 -> 参数存  到 queryString属性
            this.queryString = this.requestURI.substring( questionIndex+1 );
            this.requestURI = this.requestURI.substring( 0, questionIndex );
        }
        //协议版本  HTTP/1.1
        this.protocol = st.nextToken();
        //HTTP
        this.scheme = this.protocol.substring( 0, this.protocol.indexOf( "/" ) );

        // requestURI: /res/index.html
        //    www.baidu.com  ->  GET  /
        //contentPath: 1)/res
        //             2) /
        int slashIndex = this.requestURI.indexOf( "/",1 );
        if ( slashIndex >= 0 ){
            this.contentPath = this.requestURI.substring( 0, slashIndex );
        }else {
            this.contentPath = this.requestURI;
        }
        //requestURL: URL 统一资源定位符  http://ip:端口/requestURI
        this.requestURL = this.scheme + "://" + this.s.getLocalSocketAddress()+ this.requestURI;

        //参数的处理  :  /res/index.html?age=12&name=z
        //从  queryString 中 解析出 参数
        if (  this.queryString!= null && this.queryString.length()>0 ){
            String[] ps = this.queryString.split( "&" );
            for (  String s: ps){
                String[] params = s.split( "=" );
                //情况一  ：  uname=a
                //情况二  ：  ins=a,b,c
                this.parameterMap.put( params[0], params[1].split( "," ) );

            }
            //TODO: 还有 post的实体中也有可能有参数
        }
        this.realPath = System.getProperty("user.dir")+ File.separator+"webapps";

    }

    //从输入流中读取http请求信息( 文字 )
    private String readFromInputStream() {
        int length = -1;
        byte[] bs = new byte[  300*1024 ];  //TODO:300k 足够存 除 文件上传 之外的请求
        StringBuffer sb = null;
        try{
            length =  this.iis.read( bs , 0, bs.length );
            //将byte[] 转成 String
             sb = new StringBuffer();
            for ( int i = 0; i < length; i++){
                sb.append( (char)bs[i] );
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error( "读取输入流异常" );
        }
//       "GET /index.html HTTP/1.1\r\nHost: example.com\r\nUser-Agent: Mozilla/5.0\r\n\r\n"
        return sb.toString();
    }

    public String[] getParameterValues( String name){
        if ( parameterMap == null || parameterMap.size()<=0 ){
            return null;
        }
        String[] values = parameterMap.get( name );
        return values;
    }
    private String getParameter(String name){
        String[] values = getParameterValues( name );
        if (  values == null || values.length<=0){
            return null;
        }
        return values[0];
    }

    private String getMethod(){
        return this.method;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getContentPath() {
        return contentPath;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public String getScheme() {
        return scheme;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRealPath() {
        return realPath;
    }
}
