package i2f.commons.component.http.axios;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/1/18 16:58
 * @desc
 */
public class AxiosConfig {
    public String url;
    public AxiosMethod method;
    public AxiosHeader headers;
    public Map<String,Object> params;
    public Map<String,Object> datas;
    public String reqType;
    public String respType;
    public AxiosConfig(){}
    public AxiosConfig url(String url){
        this.url=url;
        return this;
    }
    public AxiosConfig method(AxiosMethod method){
        this.method=method;
        return this;
    }
    public AxiosConfig get(){
        this.method=AxiosMethod.GET;
        return this;
    }
    public AxiosConfig post(){
        this.method=AxiosMethod.POST;
        return this;
    }
    public AxiosConfig put(){
        this.method=AxiosMethod.PUT;
        return this;
    }
    public AxiosConfig delete(){
        this.method=AxiosMethod.DELETE;
        return this;
    }
    public AxiosConfig headers(Map<String,String> headers){
        for(Map.Entry<String,String> entry : headers.entrySet()){
            header(entry.getKey(), entry.getValue());
        }
        return this;
    }
    public AxiosConfig header(String key,String val){
        if(this.headers==null){
            this.headers=new AxiosHeader();
        }
        this.headers.put(key,val);
        return this;
    }
    public AxiosConfig params(Map<String,Object> params){
        for(Map.Entry<String,Object> entry : params.entrySet()){
            param(entry.getKey(), entry.getValue());
        }
        return this;
    }
    public AxiosConfig param(String key,Object val){
        if(this.params==null){
            this.params=new HashMap<>();
        }
        this.params.put(key,val);
        return this;
    }
    public AxiosConfig datas(Map<String,Object> datas){
        for(Map.Entry<String,Object> entry : datas.entrySet()){
            data(entry.getKey(), entry.getValue());
        }
        return this;
    }
    public AxiosConfig data(String key,Object val){
        if(this.datas==null){
            this.datas=new HashMap<>();
        }
        this.datas.put(key,val);
        return this;
    }
}
