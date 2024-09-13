package com.yc.tomcat1;

import java.io.*;

/*
    根据request请求信息  处理响应
          1xx:
          2xx:有这个资源，正常响应
          3xx:重定向，缓存
          4xx:没有这个资源 ，即没有 request 指定的文件
          5xx:服务器内部错误
 */
public class YcHttpServletResponse {
    private OutputStream oos;
    private YcHttpServletRequest request;

	public YcHttpServletResponse(YcHttpServletRequest request, OutputStream oos) {
		this.oos=oos;
		this.request=request;
	}

    public void send() {
        String url = this.request.getRequestURI();  // /jd/index.html
        String realPath = this.request.getRealPath();  // /usr/local/tomcat/webapps/jd/index.html 服务器路径
        File file = new File( realPath , url );
        byte[] fileContent = null;
        String responseProtocol = null;
        if (  !file.exists() ){
            //文件不存在 404  响应
            fileContent =  readFile( new File( realPath + File.separator, "/404.html" )  );
            responseProtocol = gen404(  fileContent );
        }else {
            //文件存在 2xx  响应
            fileContent = readFile( new File( realPath , url ) );
            responseProtocol = gen200(  fileContent );

        }
       try {
           oos.write( responseProtocol.getBytes() );
           oos.flush();
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

    private String gen200(byte[] fileContent) {
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
