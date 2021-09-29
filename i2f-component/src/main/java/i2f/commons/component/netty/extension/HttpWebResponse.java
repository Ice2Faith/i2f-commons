package i2f.commons.component.netty.extension;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

import java.nio.charset.Charset;

/**
 * @author ltb
 * @date 2021/8/18
 */
@Data
public class HttpWebResponse {
    private HttpHeaders headers=new DefaultHttpHeaders();
    private HttpResponseStatus status;
    private ByteBuf content;
    private Charset charset=Charset.forName("UTF-8");
    private String contentType="application/json;charset="+charset.displayName();
    public void ok(ByteBuf content){
        this.status=HttpResponseStatus.OK;
        this.content=content;
    }

    public void ok(String content,String charset,String ContentType){
        this.status=HttpResponseStatus.OK;
        this.content= Unpooled.copiedBuffer(content, Charset.forName(charset));
        headers.set("Content-Type",ContentType);
    }
    public void ok(String content){
        this.status=HttpResponseStatus.OK;
        this.content= Unpooled.copiedBuffer(content, charset);
        headers.set("Content-Type",contentType);
    }
}
