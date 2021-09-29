package i2f.commons.core.utils.str;

import java.net.URLEncoder;

public class HtmlUtil {
    public static String[][] plainText2HtmlMap={
            {"&","&amp;"},
            {"<","&lt;"},
            {">","&gt;"},
            {"\\\"","&quot;"},
            {"\\n","<br/>\n"},
            {"    ","&emsp;"},
            {" ","&nbsp;"},
            {"\\t","&emsp;"},
            {"\\r",""},
    };
    public static String[][] html2PlainTextMap={
            {"\t","&emsp;"},
            {" ","&nbsp;"},
            {"\"","&quot;"},
            {">","&gt;"},
            {"<","&lt;"},
            {"&","&amp;"},
    };
    public static String plainTxt2Html(String plainTxt){
        for(String[] item : plainText2HtmlMap){
            plainTxt=plainTxt.replaceAll(item[0],item[1]);
        }
        return plainTxt;
    }
    public static String jsCode(String srcCode){
        return AppendUtil.str("<script>\n",srcCode,"</script>\n");
    }
    public static String cssCode(String srcCode){
        return AppendUtil.str("<style>\n",srcCode,"</style>\n");
    }
    public static String jsAlert(String content){
        return jsCode(AppendUtil.str("alert('", content,"');\n"));
    }
    public static String html2PlainTxt(String html){
        html=html.replaceAll("<br\\s*\\/>","\n");
        StringBuffer buffer=new StringBuffer();
        int index=0;
        int len=html.length();
        while (index<len){
            if(html.charAt(index)=='<'){
                while (index<len && html.charAt(index)!='>'){
                    index++;
                }
                if(html.charAt(index)=='>'){
                    index++;
                }

            }else{
                buffer.append(html.charAt(index));
                index++;
            }
        }
        String str=buffer.toString();
        for(String[] item : html2PlainTextMap){
            str=str.replaceAll(item[1],item[0]);
        }
        return str;
    }
}
