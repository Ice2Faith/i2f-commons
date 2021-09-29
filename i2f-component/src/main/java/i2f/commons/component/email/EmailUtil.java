package i2f.commons.component.email;

import com.sun.mail.util.MailSSLSocketFactory;
import i2f.commons.component.email.data.EmailSendData;
import i2f.commons.component.email.data.EmailConfigData;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class EmailUtil {
    public static void directSend(EmailConfigData config, EmailSendData data) throws GeneralSecurityException, MessagingException {
        Session session=getSession(config);
        MimeMessage message=makeMimeMessage(config);
        fillMimeMessage(message,data);
        send(message);
    }

    public static Session getSession(String sendHost){
        Properties properties=System.getProperties();
        properties.setProperty("mail.stmp.host",sendHost);

        Session session=Session.getInstance(properties);
        return session;
    }
    public static Session getSession(EmailConfigData config) throws GeneralSecurityException {
        Properties properties=System.getProperties();
        properties.setProperty("mail.smtp.host",config.server);
        properties.setProperty("mail.smtp.port",String.valueOf(config.port));

        if(config.useSSL){
            MailSSLSocketFactory sslSocketFactory = new MailSSLSocketFactory();
            sslSocketFactory.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sslSocketFactory);
        }

        Session session=null;
        if(config.useAuth){
            properties.setProperty("mail.smtp.auth","true");
//            properties.setProperty("mail.smtp.user",config.user);
//            properties.setProperty("mail.smtp.password",config.password);
            session=Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.user, config.password);
                }
            });

        }else{
            session=Session.getInstance(properties);
        }

        return session;
    }

    public static MimeMessage makeMimeMessage(Session session,String from,String[] to) throws MessagingException {
        MimeMessage message=new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        InternetAddress[] addrs=new InternetAddress[to.length];
        for(int i=0;i<to.length;i++){
            addrs[i]=new InternetAddress(to[i]);
        }
        message.addRecipients(Message.RecipientType.TO,addrs);
        return message;
    }
    public static void send(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }
    public static MimeMessage makeMimeMessage(EmailConfigData config) throws MessagingException, GeneralSecurityException {
        Session session=getSession(config);

        MimeMessage message=makeMimeMessage(session, config.from, config.to);

        if(config.cc!=null && config.cc.length>0){
            InternetAddress[] addrs=new InternetAddress[config.cc.length];
            for(int i=0;i<config.cc.length;i++){
                addrs[i]=new InternetAddress(config.cc[i]);
            }
            message.addRecipients(Message.RecipientType.CC,addrs);
        }
        if(config.bcc!=null && config.bcc.length>0){
            InternetAddress[] addrs=new InternetAddress[config.bcc.length];
            for(int i=0;i<config.bcc.length;i++){
                addrs[i]=new InternetAddress(config.bcc[i]);
            }
            message.addRecipients(Message.RecipientType.BCC,addrs);
        }

        return message;
    }

    public static MimeMessage fillMimeMessage(MimeMessage message,EmailSendData data) throws MessagingException {
        message.setSubject(data.title,data.charSet);

        if(data.attachFiles!=null && data.attachFiles.size()>0){
            Multipart multipart=new MimeMultipart();
            if(data.type==EmailSendData.Type.PLAIN_TEXT){
                BodyPart part=genPlainTextBodyPart(data.content, data.charSet);
                multipart.addBodyPart(part);
            }else if(data.type==EmailSendData.Type.HTML_TEXT){
                BodyPart part=genHtmlTextBodyPart(data.content, data.charSet);
                multipart.addBodyPart(part);
            }
            for(String item : data.attachFiles){
                BodyPart part=genAttachmentFileBodyPart(item);
                multipart.addBodyPart(part);
            }
            message.setContent(multipart);
        }else{
            if(data.type==EmailSendData.Type.PLAIN_TEXT){
                message.setText(String.valueOf(data.content),data.charSet);
            }else if(data.type==EmailSendData.Type.HTML_TEXT){
                message.setContent(data.content,"text/html;charset="+data.charSet);
            }
        }

        return message;
    }

    public static BodyPart genAttachmentFileBodyPart(String fileName) throws MessagingException {
        File file=new File(fileName);
        BodyPart part=new MimeBodyPart();
        DataSource source=new FileDataSource(file.getAbsolutePath());
        part.setDataHandler(new DataHandler(source));
        part.setFileName(file.getName());
        return part;
    }

    public static BodyPart genPlainTextBodyPart(String content,String charSet) throws MessagingException {
        BodyPart part=new MimeBodyPart();
        part.setContent(content,"text/plain;charset="+charSet);
        return part;
    }

    public static BodyPart genHtmlTextBodyPart(String html,String charSet) throws MessagingException {
        BodyPart part=new MimeBodyPart();
        part.setContent(html,"text/html;charset="+charSet);
        return part;
    }

}
