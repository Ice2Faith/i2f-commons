package i2f.commons.component.weather;


import i2f.commons.component.restapi.AmapUtil;
import i2f.commons.component.restapi.data.ip.Ip2RespData;
import i2f.commons.component.weather.data.CommonWeatherRespData;

import java.io.IOException;

public class CaiYunAmapIpWeatherUtil {
    public static CommonWeatherRespData getIpWeather(String ip) throws IOException {
        Ip2RespData ip2= AmapUtil.getIp2City(ip);
        CommonWeatherRespData data= CaiYunWeatherUtil.getCommonWeather(ip2.getLocation());
        return data;
    }
}
