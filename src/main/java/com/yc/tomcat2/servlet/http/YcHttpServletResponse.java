package com.yc.tomcat2.servlet.http;

import com.yc.tomcat2.servlet.YcServletResponse;

import java.io.*;

/*
    根据request请求信息  处理响应
          1xx:
          2xx:有这个资源，正常响应
          3xx:重定向，缓存
          4xx:没有这个资源 ，即没有 request 指定的文件
          5xx:服务器内部错误
 */
public class YcHttpServletResponse implements YcServletResponse {
    private OutputStream oos;
    private YcHttpServletRequest request;

	public YcHttpServletResponse(YcHttpServletRequest request, OutputStream oos) {
		this.oos=oos;
		this.request=request;
	}

    /*
        响应  发送 响应协议
     */
    @Override
    public void send() {
        String url = this.request.getRequestURI();  //  获取请求的 URI，  /jd/index.html
        String realPath = this.request.getRealPath();  // 获取服务器路径， /usr/local/tomcat/webapps/jd/index.html 服务器路径
        File file = new File( realPath , url ); //// 创建一个 File 对象，表示请求的文件
        byte[] fileContent = null;
        String responseProtocol = null;

        if (  !file.exists() ){
            //文件不存在 404  响应  返回 404 错误页面
            fileContent =  readFile( new File( realPath + File.separator, "/404.html" )  );
            responseProtocol = gen404(  fileContent );
        }else {
            //文件存在 2xx  响应
            fileContent = readFile( new File( realPath , url ) );  // 读取请求文件的内容
            responseProtocol = gen200(  fileContent );// 生成 200 响应协议
        }
       try {
           oos.write( responseProtocol.getBytes() ); // 写入响应协议到输出流
           oos.flush();// 刷新输出流
           oos.write( fileContent );
           oos.flush();
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           if (  oos!= null){
               try {
                   this.oos.close();
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }
       }
    }

    @Override
    public OutputStream getOutputStream() {
        return oos;
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter( oos );
    }

    private String gen200( byte[] fileContent) {
        String protocol200 = "";
        //先取出 请求的资源类型
        String url = this.request.getRequestURI();//url   /jd/index.html
        //以 url 取后缀名
        int index = url.lastIndexOf( "." );
        if ( index >= 0 ){
            index = index+1;
        }
        //TODO 引入 策略模式来读取  server.xml 中配置的 资源类型  来读取 响应头
        String fileExtension = url.substring( index );
        if ("JPG".equalsIgnoreCase( fileExtension ) ){
            protocol200 = "HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\nContent-Length: " + fileContent.length+"\r\n\r\n";
        } else if ("css".equalsIgnoreCase( fileExtension )) {
            protocol200 = "HTTP/1.1 200 OK\r\nContent-Type: text/css\r\nContent-Length: " + fileContent.length+"\r\n\r\n";
        }  else if ("js".equalsIgnoreCase( fileExtension )) {
            protocol200 = "HTTP/1.1 200 OK\r\nContent-Type: application/javascript\r\nContent-Length: " + fileContent.length+"\r\n\r\n";
        } else if ("gif".equalsIgnoreCase( fileExtension )) {
            protocol200 = "HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\nContent-Length: " + fileContent.length+"\r\n\r\n";
        } else if ("png".equalsIgnoreCase( fileExtension )) {
            protocol200 = "HTTP/1.1 200 OK\r\nContent-Type: image/png\r\nContent-Length: " + fileContent.length+"\r\n\r\n";
        }else{
            protocol200 = "HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: " + fileContent.length+"\r\n\r\n";
        }
        return protocol200;
    }

    private String gen404(byte[] fileContent) {
        String protocol404 = "HTTP/1.1 404 Not Found\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: " + fileContent.length+"\r\n";
        protocol404 += "Server: kitty server\r\n\r\n";
        return protocol404;
    }

    /*
        读取 本地 文件
     */
    private byte[] readFile(File file) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        try {
            byte[] buf = new byte[ 100 * 1024 ];
            int length = -1;
            fis = new FileInputStream( file );
            while ( ( length = fis.read( buf,0,buf.length ) )!= -1 ){
                bos.write( buf, 0, length );
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if ( fis!= null ){
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bos.toByteArray();
    }
}
