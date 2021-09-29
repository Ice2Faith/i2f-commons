package i2f.commons.component.netty.extension;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import lombok.Data;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/8/18
 */
@Data
public class HttpWebRequest {
    private FullHttpRequest fullHttpRequest;
    private HttpMethod method;
    private URI uri;
    private HttpHeaders headers;
    private Map<String,Object> urlParams=new HashMap<>();
    private Map<String,Object> bodyParams=new HashMap<>();
    private byte[] content;

}
