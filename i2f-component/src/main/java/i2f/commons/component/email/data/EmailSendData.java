package i2f.commons.component.email.data;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class EmailSendData {
    public interface Type{
        int PLAIN_TEXT=0;
        int HTML_TEXT=1;
    }
    public int type= Type.PLAIN_TEXT;

    public String title;
    public String content;

    public String charSet="UTF-8";

    public Set<String> attachFiles;

    public static EmailSendData builder(){
        return new EmailSendData();
    }

    public EmailSendData setType(int type){
        this.type=type;
        return this;
    }

    public EmailSendData setCharSet(String charSet){
        this.charSet=charSet;
        return this;
    }

    public EmailSendData setTitle(String title){
        this.title=title;
        return this;
    }

    public EmailSendData setContent(String content){
        this.content=content;
        return this;
    }

    public EmailSendData attach(String ... fileNames){
        if(fileNames==null || fileNames.length==0){
            return this;
        }
        if(this.attachFiles==null){
            this.attachFiles=new HashSet<String>();
        }
        for(String item : fileNames){
            this.attachFiles.add(item);
        }
        return this;
    }

    public EmailSendData build(){
        return this;
    }

}
