package i2f.commons.component.email.data;

import lombok.Data;

@Data
public class EmailConfigData {
    //https://blog.csdn.net/lizhongfu2013/article/details/108212640
    public interface Host{
        String POP3_SINA_COM="pop3.sina.com.cn";
        String SMTP_SINA_COM="smtp.sina.com.cn";
        int POP3_SINA_COM_PORT=110;
        int SMTP_SINA_COM_PORT=25;

        String POP3_SINA_CN="pop3.sina.com";  //pop.sina.com
        String SMTP_SINA_CN="smtp.sina.com";
        int POP3_SINA_CN_PORT=110;
        int SMTP_SINA_CN_PORT=25;

        String POP3_SINA_VIP="pop3.vip.sina.com";
        String SMTP_SINA_VIP="smtp.vip.sina.com";
        int POP3_SINA_VIP_PORT=110;
        int SMTP_SINA_VIP_PORT=25;

        String POP3_SOHU_COM="pop3.sohu.com";
        String SMTP_SOHU_COM="smtp.sohu.com";
        int POP3_SOHU_COM_PORT=110;
        int SMTP_SOHU_COM_PORT=25;

        String POP3_126_COM="pop.126.com";
        String SMTP_126_COM="smtp.126.com";
        int POP3_126_COM_PORT=110;
        int SMTP_126_COM_PORT=25;

        String POP3_139_COM="pop.139.com";
        String SMTP_139_COM="smtp.139.com";
        int POP3_139_COM_PORT=110;
        int SMTP_139_COM_PORT=25;

        String POP3_163_COM="pop.163.com";
        String SMTP_163_COM="smtp.163.com";
        int POP3_163_COM_PORT=110;
        int SMTP_163_COM_PORT=25;

        String POP3_QQ_COM="pop.qq.com";
        String SMTP_QQ_COM="smtp.qq.com";
        int POP3_QQ_COM_PORT=110;
        int SMTP_QQ_COM_PORT=25;

        String POP3_EXMAIL_QQ_COM="pop.exmail.qq.com"; //ssl
        String SMTP_EXMAIL_QQ_COM="smtp.exmail.qq.com"; //ssl
        int POP3_EXMAIL_QQ_COM_PORT=995;
        int SMTP_EXMAIL_QQ_COM_PORT=587;//465

        String POP3_YAHOO_COM="pop.mail.yahoo.com";
        String SMTP_YAHOO_COM="smtp.mail.yahoo.com";

        String POP3_YAHOO_COM_CN="pop.mail.yahoo.com.cn";
        String SMTP_YAHOO_COM_CN="smtp.mail.yahoo.com.cn";
        int POP3_YAHOO_COM_CN_PORT=995;
        int SMTP_YAHOO_COM_CN_PORT=587;

        String POP3_LIVE_COM="pop3.live.com";
        String SMTP_LIVE_COM="smtp.live.com";
        int POP3_LIVE_COM_PORT=995;
        int SMTP_LIVE_COM_PORT=587;

        String POP3_GMAIL_COM="pop.gmail.com";
        String SMTP_GMAIL_COM="smtp.gmail.com";
        int POP3_GMAIL_COM_PORT=995;
        int SMTP_GMAIL_COM_PORT=587;

        String POP3_263_NET="pop3.263.net";
        String SMTP_263_NET="smtp.263.net";
        int POP3_263_NET_PORT=110;
        int SMTP_263_NET_PORT=25;

        String POP3_263_NET_CN="pop.263.net.cn";
        String SMTP_263_NET_CN="smtp.263.net.cn";
        int POP3_263_NET_CN_PORT=110;
        int SMTP_263_NET_CN_PORT=25;

        String POP3_X263_NET="pop.x263.net";
        String SMTP_X263_NET="smtp.x263.net";
        int POP3_X263_NET_PORT=110;
        int SMTP_X263_NET_PORT=25;

        String POP3_21CN_COM="pop.21cn.com";
        String SMTP_21CN_COM="smtp.21cn.com";
        int POP3_21CN_COM_PORT=110;
        int SMTP_21CN_COM_PORT=25;

        String POP3_FOXMAIL_COM="pop.foxmail.com";
        String SMTP_FOXMAIL_COM="smtp.foxmail.com";
        int POP3_FOXMAIL_COM_PORT=110;
        int SMTP_FOXMAIL_COM_PORT=25;

        String POP3_CHINA_COM="pop.china.com";
        String SMTP_CHINA_COM="smtp.china.com";
        int POP3_CHINA_COM_PORT=110;
        int SMTP_CHINA_COM_PORT=25;

        String POP3_TOM_COM="pop.tom.com";
        String SMTP_TOM_COM="smtp.tom.com";
        int POP3_TOM_COM_PORT=110;
        int SMTP_TOM_COM_PORT=25;

        String POP3_ETANG_COM="pop.etang.com";
        String SMTP_ETANG_COM="smtp.etang.com";
    }
    public String from;
    public String[] to;

    public String[] cc;
    public String[] bcc;

    public String server;
    public int port;

    public boolean useSSL=false;

    public boolean useAuth=true;
    public String user;
    public String password;

    public static EmailConfigData builder(){
        return new EmailConfigData();
    }
    public EmailConfigData setServer(String server,int port){
        this.server=server;
        this.port=port;
        return this;
    }
    public EmailConfigData auth(boolean useAuth){
        this.useAuth=useAuth;
        return this;
    }
    public EmailConfigData setAuth(String user,String password){
        this.useAuth=true;
        this.user=user;
        this.password=password;
        return this;
    }
    public EmailConfigData ssl(boolean useSSL){
        this.useSSL=useSSL;
        return this;
    }
    public EmailConfigData sendFrom(String from){
        this.from=from;
        return this;
    }
    public EmailConfigData sendTo(String ... to){
        this.to=to;
        return this;
    }
    public EmailConfigData copyTo(String ... cto){
        this.cc=cto;
        return this;
    }
    public EmailConfigData secreteCopyTo(String ... ccto){
        this.bcc=ccto;
        return this;
    }
    public EmailConfigData build(){
        return this;
    }

}
