package RSS.bean;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
    解析RSS源
 */
public class RSSDataCapturer extends DefaultHandler {
    private  static  final org.apache.log4j.Logger log = Logger.getLogger(RSSDataCapturer.class);
    private List<CommonInformationItem> list;
    private CommonInformationItem item;
    private String source;  //RSS的源名称
    private int status; // 存当前解析到什么标记
    private StringBuffer buffer; //存文本
    private DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public RSSDataCapturer(String source){
        this.source = source;  //RSS的源名称
        buffer = new StringBuffer();
    }

    /*
        解析 source对应的RSS源 得到item集合  返回
     */
    public List<CommonInformationItem> load( String path ) {
        list = new ArrayList<>();
        try {
            //SAX解析流程代码
            //1.创建SAX解析器
            SAXParserFactory spf = SAXParserFactory.newInstance();
            //2.注册事件处理器
            SAXParser parser = spf.newSAXParser();
            //3.解析xml文件
            parser.parse( path, this);
        }catch (Exception e){
            e.printStackTrace();
            log.error( e );
        }
        return  list;
    }

    //解析到xml文件的开头
    @Override
    public void startDocument() throws SAXException {
//        log.debug("文档开头");
        super.startDocument();
    }
    //解析到xml文件的结尾
    @Override
    public void endDocument() throws SAXException {
//        log.debug("文档结尾");
        super.endDocument();
    }

    private final int STATUS_TITLE = 1;
    private final int STATUS_LINK = 2;
    private final int STATUS_DESCRIPTION = 3;
    private final int STATUS_PUBDATE = 4;
    private final int STATUS_GUID = 5;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        log.debug("解析到一个元素的开始标签:url:"+uri+"\tlocalName:"+localName+"\t qName:"+qName+"\tattributes:"+attributes);
        if(  item!=null &&  qName.equalsIgnoreCase("title") ){
            status = STATUS_TITLE;
        }else if (  item!=null && qName.equalsIgnoreCase("guid")){
            status = STATUS_GUID;
        }else if ( item!=null && qName.equalsIgnoreCase("description")){
            status = STATUS_DESCRIPTION;
        }else if ( item!=null && qName.equalsIgnoreCase("link")){
            status = STATUS_LINK;
        }else if ( item!=null && qName.equalsIgnoreCase("pubDate")){
            status = STATUS_PUBDATE;
        }else if ( qName.equalsIgnoreCase("item")){
            item = new CommonInformationItem();
            item.setSource(    this.source    );
        }
    }
    //解析到一个元素的结束标签
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //  </title>  ...
//        log.debug("解析到一个元素的结束标签:uri:"+uri+"\tlocalName:"+localName+"\tqName:"+qName);
        //判断解析的是哪个的结束标签
        if ( item!=null && qName.equalsIgnoreCase("title")){
            item.setTitle( buffer.toString() );
            status = 0;
        }else if ( item!=null && qName.equalsIgnoreCase("link")){
            item.setLink( buffer.toString() );
            status = 0;
        }else if ( item!=null && qName.equalsIgnoreCase("description")){
            item.setDescription( buffer);
            status = 0;
        }else if ( item!=null && qName.equalsIgnoreCase("pubDate")){
            item.setTxtDate( buffer.toString() );
            try{
                item.setDate( df.parse( buffer.toString().trim() ) );
            }catch (Exception e){
                log.error( e );
            }
            status = 0;
        }else if ( item!=null && qName.equalsIgnoreCase("guid")){
            item.setId( buffer.toString() );
            status = 0;
        }else if ( qName.equalsIgnoreCase("item")){
            if ( item.getId()== null ){
                item.setId( item.getDescription().hashCode()+""  );
            }
            //上面的其它标签都解析完了 每一条新闻都是一个item  一条新闻: <item> 中间为其它标签及内容 。。。</item>
            // 最后 把 每一条 item 放到 list 中
            list.add(  item );
            item = null;
        }
        buffer = new StringBuffer();
    }
    //解析到一个元素文本节点
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String txt = new String( ch,start,length );
//        log.debug("解析到一个元素的文本节点:"+new String( ch,start,length ));
        buffer.append( txt );
    }
}
