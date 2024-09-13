package com.yc.tomcat2;

import com.yc.tomcat2.servlet.YcServletRequest;
import com.yc.tomcat2.servlet.YcServletResponse;
import com.yc.tomcat2.servlet.http.YcHttpServletResponse;

public class StaticProcessor implements Processor{

    @Override
    public void process(YcServletRequest request, YcServletResponse response) {
//        ((YcHttpServletResponse)response).send();
        response.send();
    }
}
