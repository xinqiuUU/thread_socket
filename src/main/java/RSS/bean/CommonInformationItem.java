package RSS.bean;

import lombok.Data;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

/*
    存每一条新闻的信息
 */
@Data
public class CommonInformationItem implements Serializable {
    private String title;   // 标题 item的title
    private String txtDate; // 从 xml 中得到的是 String 日期
    private Date date;      // 转为Date
    private String link;    // 链接 item的link
    private StringBuffer description; // 内容 item的描述
    private String id;      //item的id
    private String source;  // RSS的源名称

    public void addDescription(String txt) {
        this.description.append( txt );
    }
    /*
        每条新闻生成 的 文件名
     */
    public String getFileName() {
        StringWriter writer = new StringWriter();
        writer.append( source );
        writer.append( "_" );
        if ( description != null ){
            writer.append(String.valueOf( description.hashCode() ) );
        }else {
            writer.append( String.valueOf(System.currentTimeMillis()) );
        }
        writer.append( ".xml");
        return writer.toString();
    }

}
