package i2f.commons.component.http;


import i2f.commons.component.http.data.HttpRequestData;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.data.Pair;
import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.json.Json2;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.xml.Xml2;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
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

    public static Map<String, Object> inflateListPair2ObjectMode(List<Pair<String,Object>> list){
        if(list==null){
            return null;
        }
        Map<String,Object> map=new HashMap<>();
        for(Pair<String,Object> item : list){
            if(map.containsKey(item.key)){
                Object val=map.get(item.key);
                if(val instanceof List){
                    ((List)val).add(item.val);
                }else{
                    List arr=new ArrayList();
                    arr.add(val);
                    arr.add(item.val);
                }
            }else{
                map.put(item.key,item.val);
            }
        }
        return map;
    }

    public static<T> T requestObj(HttpRequestData cfgData,Class<T> clazz) throws IOException {
        return request(cfgData, new IMap<CloseableHttpResponse, T>() {
            @Override
            public T map(CloseableHttpResponse response) {
                HttpEntity entity=response.getEntity();
                T ret= null;
                try {
                    String content = EntityUtils.toString(entity);
                    ret=JsonUtil.fromJson(content,clazz);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }

    public static String request(HttpRequestData cfgData) throws IOException {
        return request(cfgData, new IMap<CloseableHttpResponse, String>() {
            @Override
            public String map(CloseableHttpResponse response) {
                HttpEntity entity=response.getEntity();
                String ret= null;
                try {
                    ret = EntityUtils.toString(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }

    /**
     * 模拟Axios式调用，给定config，以及映射器，就将结果映射为实际结果
     * @param cfgData config
     * @return 映射结果
     * @throws IOException
     */
    public static<T> T request(HttpRequestData cfgData, IMap<CloseableHttpResponse,T> mapper)throws IOException{
        CloseableHttpClient httpClient=HttpClientBuilder.create()
                .build();

        String paramsStr= "";

        if(cfgData.getUrlParams()!=null && cfgData.getUrlParams().size()>0){
            boolean isFirst=true;
            StringBuilder builder=new StringBuilder();
            builder.append("?");
            for(Pair<String,Object> item : cfgData.getUrlParams()){
                if(!isFirst){
                    builder.append("&");
                }
                builder.append(item.getKey())
                        .append("=");
                Object val=item.getVal();
                if(val!=null){
                    String valStr=String.valueOf(val);
                    valStr=URLEncoder.encode(valStr,"UTF-8");
                    builder.append(valStr);
                }
                isFirst=false;
            }
            paramsStr= builder.toString();
        }


        String reqUrl= cfgData.getUrl()+paramsStr;

        RequestConfig config= RequestConfig.custom()
                .setConnectTimeout(cfgData.getTimeout())
                .setConnectionRequestTimeout(cfgData.getTimeout())
                .setSocketTimeout(cfgData.getTimeout())
                .setRedirectsEnabled(cfgData.isAllowRedirect())
                .setCircularRedirectsAllowed(cfgData.isAllowRedirect())
                .setProxy(cfgData.getProxy())
                .build();

        HttpUriRequest req=null;

        HttpRequestData.RequestMethod method=cfgData.getMethod();
        if(method== HttpRequestData.RequestMethod.GET){
            HttpGet httpGet=new HttpGet(reqUrl);
            req=httpGet;
            httpGet.setConfig(config);

        }else if(method== HttpRequestData.RequestMethod.POST){
            HttpPost httpPost=new HttpPost(reqUrl);
            req=httpPost;
            httpPost.setConfig(config);
            if(cfgData.getBodyParams()!=null){
                HttpRequestData.RequestType type=cfgData.getType();
                if(type== HttpRequestData.RequestType.FORM){
                    List<NameValuePair> paramsList=new ArrayList<>();

                    for(Pair<String,Object> item : cfgData.getBodyParams()){
                        String val=item.val==null?"":String.valueOf(item.val);
                        NameValuePair pair=new BasicNameValuePair(item.key, val);
                        paramsList.add(pair);
                    }

                    httpPost.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
                }else if(type== HttpRequestData.RequestType.JSON){
                    Map<String,Object> mobj=inflateListPair2ObjectMode(cfgData.getBodyParams());
                    String content=Json2.toJson(mobj);
                    httpPost.setEntity(new StringEntity(content,"application/json","utf-8"));
                }else if(type== HttpRequestData.RequestType.XML){
                    Map<String,Object> mobj=inflateListPair2ObjectMode(cfgData.getBodyParams());
                    String content= Xml2.toXml(mobj);
                    httpPost.setEntity(new StringEntity(content,"application/xml","utf-8"));
                }
            }
        }else if(method== HttpRequestData.RequestMethod.PUT){
            HttpPut httpPut=new HttpPut(reqUrl);
            req=httpPut;
            httpPut.setConfig(config);
            if(cfgData.getBodyParams()!=null){
                HttpRequestData.RequestType type=cfgData.getType();
                if(type== HttpRequestData.RequestType.FORM){
                    List<NameValuePair> paramsList=new ArrayList<>();

                    for(Pair<String,Object> item : cfgData.getBodyParams()){
                        String val=item.val==null?"":String.valueOf(item.val);
                        NameValuePair pair=new BasicNameValuePair(item.key, val);
                        paramsList.add(pair);
                    }

                    httpPut.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
                }else if(type== HttpRequestData.RequestType.JSON){
                    Map<String,Object> mobj=inflateListPair2ObjectMode(cfgData.getBodyParams());
                    String content=Json2.toJson(mobj);
                    httpPut.setEntity(new StringEntity(content,"application/json","utf-8"));
                }else if(type== HttpRequestData.RequestType.XML){
                    Map<String,Object> mobj=inflateListPair2ObjectMode(cfgData.getBodyParams());
                    String content= Xml2.toXml(mobj);
                    httpPut.setEntity(new StringEntity(content,"application/xml","utf-8"));
                }
            }
        }else if(method== HttpRequestData.RequestMethod.DELETE){
            HttpDelete httpDelete=new HttpDelete(reqUrl);
            req=httpDelete;
            httpDelete.setConfig(config);
        }

        if(cfgData.getHeaders()!=null &&cfgData.getHeaders().size()>0){
            for(Pair<String,Object> item : cfgData.getHeaders()){
                String val=item.val==null?"":String.valueOf(item.val);
                req.addHeader(item.key,val);
            }
        }

        CloseableHttpResponse response=null;

        try{
            response=httpClient.execute(req);

            T ret=mapper.map(response);

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
