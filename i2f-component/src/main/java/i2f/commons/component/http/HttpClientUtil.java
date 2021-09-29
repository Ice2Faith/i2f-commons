package i2f.commons.component.http;


import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
    public static Map<String,String> toUrlParamsMap(Map<String,Object> map) {
        Map<String,String> ret=new HashMap<>();
        for(String item : map.keySet()){
            Object val=map.get(item);
            if(val==null){
                ret.put(item,"");
            }
            String sval=String.valueOf(val);
            try{
                sval=URLEncoder.encode(sval,"UTF-8");
            }catch(Exception e){

            }
            ret.put(item,sval);
        }
        return ret;
    }
    public static List<NameValuePair> toRequestParamsList(Map<String,Object> map){
        List<NameValuePair> ret=new ArrayList<NameValuePair>();
        for (String key : map.keySet()){
            Object val=map.get(key);
            if(val==null){
                ret.add(new BasicNameValuePair(key,""));
            }else{
                ret.add(new BasicNameValuePair(key,String.valueOf(val)));
            }
        }
        return ret;
    }
    public static String doGet(String url, Map<String,Object> params) throws IOException {
        CloseableHttpClient httpClient= HttpClientBuilder.create()
                .build();
        String paramsStr= "";
        if(CheckUtil.notEmptyMap(params)){
            paramsStr= AppendUtil.buffer()
                    .addMap(false,"&","?",null,"=",toUrlParamsMap(params))
                    .done();
        }
        HttpGet httpGet=new HttpGet(url+paramsStr);

        CloseableHttpResponse response=null;

        RequestConfig config= RequestConfig.custom()
                .setConnectTimeout(6000)
                .setConnectionRequestTimeout(6000)
                .setSocketTimeout(6000)
                .setRedirectsEnabled(true)
                .build();

        httpGet.setConfig(config);

        try{
            response=httpClient.execute(httpGet);

            HttpEntity entity=response.getEntity();

            String ret=EntityUtils.toString(entity);

            return ret;
        }catch(IOException e){
            throw e;
        }finally {
            if(httpClient!=null){
                httpClient.close();
            }
            if(response!=null){
                response.close();
            }
        }
    }

    public static String doPost(String url,Map<String,Object> params)throws IOException{
        CloseableHttpClient httpClient=HttpClientBuilder.create()
                .build();
        HttpPost httpPost=new HttpPost(url);

        CloseableHttpResponse response=null;

        RequestConfig config= RequestConfig.custom()
                .setConnectTimeout(6000)
                .setConnectionRequestTimeout(6000)
                .setSocketTimeout(6000)
                .setRedirectsEnabled(true)
                .build();

        httpPost.setConfig(config);

        if(CheckUtil.notEmptyMap(params)){
            List<NameValuePair> paramsList=toRequestParamsList(params);

            httpPost.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));

        }

        try{
            response=httpClient.execute(httpPost);

            HttpEntity entity=response.getEntity();

            String ret=EntityUtils.toString(entity);

            return ret;
        }catch(IOException e){
            throw e;
        }finally {
            if(httpClient!=null){
                httpClient.close();
            }
            if(response!=null){
                response.close();
            }
        }

    }

}
