package i2f.commons.component.weather;

import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.component.weather.data.CommonWeatherReqData;
import i2f.commons.component.weather.data.CommonWeatherRespData;

import java.io.IOException;

public class CaiYunWeatherUtil {
    public static String TOKEN="F77eHAJK8O36HkxC";

    //通用预报接口：(已经包含以下接口的数据，并且数据一样)
    // https://api.caiyunapp.com/v2.5/TAkhjf8d1nlSlspN/121.6544,25.1552/weather.json
    //实况天气接口：
    // https://api.caiyunapp.com/v2.5/TAkhjf8d1nlSlspN/121.6544,25.1552/realtime.json
    //分钟级降雨预报接口：
    // https://api.caiyunapp.com/v2.5/TAkhjf8d1nlSlspN/121.6544,25.1552/minutely.json
    //小时级预报接口：
    // https://api.caiyunapp.com/v2.5/TAkhjf8d1nlSlspN/121.6544,25.1552/hourly.json
    //天级预报接口：
    // https://api.caiyunapp.com/v2.5/TAkhjf8d1nlSlspN/121.6544,25.1552/daily.json

    /**
     * 参数：经纬度:x,y
     * @param geoLocation
     * @return
     * @throws IOException
     */
    public static CommonWeatherRespData getCommonWeather(String geoLocation) throws IOException {
        CommonWeatherReqData reqData=new CommonWeatherReqData();
        reqData.setToken(TOKEN);
        reqData.setGeoLocation(geoLocation);
        String url= reqData.genUrl();
        String js= HttpClientUtil.doGet(url,null);
        CommonWeatherRespData ret= JsonUtil.fromJson(js,CommonWeatherRespData.class);
        return ret;
    }
}
