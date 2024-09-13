package com.yc.tomcat2.servlet;

import java.io.OutputStream;
import java.io.PrintWriter;

public interface YcServletResponse {
    public void send();

    public OutputStream getOutputStream();

    public PrintWriter  getWriter();
}
