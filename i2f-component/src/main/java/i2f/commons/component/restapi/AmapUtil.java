package i2f.commons.component.restapi;

import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.component.restapi.data.ip.Ip2ReqData;
import i2f.commons.component.restapi.data.ip.Ip2RespData;
import i2f.commons.component.restapi.data.ip.IpReqData;
import i2f.commons.component.restapi.data.ip.IpRespData;
import i2f.commons.component.restapi.data.tips.TipsReqData;
import i2f.commons.component.restapi.data.tips.TipsRespData;
import i2f.commons.component.restapi.data.weather.WeatherReqData;
import i2f.commons.component.restapi.data.weather.WeatherRespData;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

public class AmapUtil {
    public static String APP_KEY="6bdfd5bfeb0a1083a979455e765e4fdc";
    public static Ip2RespData getIp2City(String ip) throws IOException {
        Ip2ReqData reqData=new Ip2ReqData();
        reqData.setKey(APP_KEY);
        reqData.setIp(ip);
        reqData.setType("4");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String rs= HttpClientUtil.doGet(Ip2ReqData.URL,params);
        Ip2RespData ret= JsonUtil.fromJson(rs,Ip2RespData.class);
        return ret;
    }
    public static IpRespData getMyCity() throws IOException {
        return getIpCity(null);
    }
    public static IpRespData getIpCity(String ip) throws IOException {
        IpReqData reqData=new IpReqData();
        reqData.setKey(APP_KEY);
        reqData.setIp(ip);
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String rs= HttpClientUtil.doGet(IpReqData.URL,params);
        IpRespData ret= JsonUtil.fromJson(rs,IpRespData.class);
        return ret;
    }

    public static WeatherRespData getIpCityWeather(String ip) throws IOException {
        IpRespData iprs=getIpCity(ip);
        return getCityWeather(iprs.getAdcode());
    }

    public static WeatherRespData getMyCityWeather() throws IOException {
        IpRespData iprs=getMyCity();
        return getCityWeather(iprs.getAdcode());
    }

    public static WeatherRespData getCityWeather(String adcode) throws IOException {
        WeatherReqData reqData=new WeatherReqData();
        reqData.setKey(APP_KEY);
        reqData.setCity(adcode);
        //预报
        reqData.setExtensions("all");
        reqData.setOutput("JSON");
        Map<String,Object> params=BeanResolver.toMap(reqData,true);
        String rs=HttpClientUtil.doGet(WeatherReqData.URL,params);
        WeatherRespData ret=JsonUtil.fromJson(rs,WeatherRespData.class);
        //实时
        reqData.setExtensions("base");
        params=BeanResolver.toMap(reqData,true);
        rs=HttpClientUtil.doGet(WeatherReqData.URL,params);
        WeatherRespData retBase=JsonUtil.fromJson(rs,WeatherRespData.class);

        ret.setLives(retBase.getLives());
        return ret;
    }

    public static TipsRespData getIpCityTips(String ip, String keywords) throws IOException {
        IpRespData iprs=getIpCity(ip);
        return getCityTips(iprs.getAdcode(), keywords);
    }

    public static TipsRespData getMyCityTips(String keywords) throws IOException {
        IpRespData iprs=getMyCity();
        return getCityTips(iprs.getAdcode(), keywords);
    }

    public static TipsRespData getCityTips(String adcode,String keywords) throws IOException {
        TipsReqData reqData=new TipsReqData();
        reqData.setKey(APP_KEY);
        reqData.setCity(adcode);
        reqData.setCitylimit(true);
        reqData.setKeywords(keywords);
        reqData.setOutput("JSON");
        reqData.setDatatype("all");
        reqData.setType("all");

        Map<String,Object> params=BeanResolver.toMap(reqData,true);
        String rs=HttpClientUtil.doGet(TipsReqData.URL,params);
        TipsRespData ret=JsonUtil.fromJson(rs,TipsRespData.class);
        return ret;
    }
}
