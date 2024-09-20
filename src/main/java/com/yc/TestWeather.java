package com.yc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TestWeather {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("ws.webxml.com.cn", 80);

            // 构建GET请求
            String request = "GET /WebServices/WeatherWS.asmx/getWeather?theCityCode=1780&theUserID= HTTP/1.1\n" +
                    "User-Agent: 666\r\n"+
                    "Host: ws.webxml.com.cn\n" +
                    "Connection: close\n\n";

            // 发送请求
            OutputStream os = socket.getOutputStream();
            os.write(request.getBytes("UTF-8"));
            os.flush();

            // 读取响应
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            StringBuilder response = new StringBuilder();
            while ((bytesRead = is.read(buffer)) != -1) {
                response.append(new String(buffer, 0, bytesRead, "UTF-8"));
            }

            System.out.println(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
