package RSS.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
    读取 project.properties 中 配置文件
 */
public class ProjectProperties extends Properties {

    private static ProjectProperties instance;
    private ProjectProperties(){
        //读取配置文件
        InputStream iis= ProjectProperties.class.getClassLoader().getResourceAsStream("project.properties");
        //Properties类的load方法加载
        try {
            this.load(    iis );    //   this就是 DbProperties  对象，
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public static ProjectProperties getInstance(){
        if(   instance==null){
            instance=new ProjectProperties();
        }
        return instance;
    }

}
