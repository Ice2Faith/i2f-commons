package i2f.commons.component.http.data;

import i2f.commons.core.data.Pair;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpHost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/10/15
 */
@Data
@NoArgsConstructor
public class HttpRequestData {
    private String url;
    private List<Pair<String,Object>> urlParams;
    private List<Pair<String,Object>> bodyParams;
    private List<Pair<String,Object>> headers;
    private RequestMethod method=RequestMethod.GET;
    private RequestType type=RequestType.FORM;
    private int timeout=30*1000;
    private boolean allowRedirect=true;
    private HttpHost proxy;

    public enum RequestMethod{
        GET("GET"),POST("POST"),PUT("PUT"),DELETE("DELETE");

        private String method;
        private RequestMethod(String method){
            this.method=method;
        }
        public String getMethod(){
            return this.method;
        }
    }

    public enum RequestType{
        JSON("JSON"),XML("XML"),FORM("FORM");

        private String type;
        private RequestType(String type){
            this.type=type;
        }
        public String getType(){
            return this.type;
        }
    }

    public HttpRequestData url(String url){
        this.url=url;
        return this;
    }

    public HttpRequestData type(RequestType type){
        this.type=type;
        return this;
    }

    public HttpRequestData method(RequestMethod method){
        this.method=method;
        return this;
    }

    public HttpRequestData timeout(int timeout){
        this.timeout=timeout;
        return this;
    }

    public HttpRequestData allowRedirect(boolean allowRedirect){
        this.allowRedirect=allowRedirect;
        return this;
    }
    public HttpRequestData proxy(HttpHost proxy){
        this.proxy=proxy;
        return this;
    }

    public HttpRequestData addHeader(String key,Object val){
        if(headers==null){
            headers=new ArrayList<>();
        }
        headers.add(new Pair<>(key,val));
        return this;
    }

    public HttpRequestData addHeaders(Map<String,Object> map){
        for(Map.Entry<String, Object> item : map.entrySet()){
            addHeader(item.getKey(),item.getValue());
        }
        return this;
    }

    public HttpRequestData addUrlParam(String key,Object val){
        if(urlParams==null){
            urlParams=new ArrayList<>();
        }
        urlParams.add(new Pair<>(key,val));
        return this;
    }

    public HttpRequestData addUrlParams(Map<String,Object> map){
        for(Map.Entry<String, Object> item : map.entrySet()){
            addUrlParam(item.getKey(),item.getValue());
        }
        return this;
    }

    public HttpRequestData addBodyParam(String key,Object val){
        if(bodyParams==null){
            bodyParams=new ArrayList<>();
        }
        bodyParams.add(new Pair<>(key,val));
        return this;
    }

    public HttpRequestData addBodyParams(Map<String,Object> map){
        for(Map.Entry<String, Object> item : map.entrySet()){
            addBodyParam(item.getKey(),item.getValue());
        }
        return this;
    }

}
