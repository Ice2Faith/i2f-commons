package i2f.commons.component.restapi.data.weather;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherReqData {
    public static final String URL="https://restapi.amap.com/v3/weather/weatherInfo";
    public static final String METHOD="GET";
    private String key;//请求服务权限标识
    private String city;//城市编码,输入城市的adcode，adcode信息
    private String extensions;//可选值：base/all,base:返回实况天气,all:返回预报天气
    private String output;//可选值：JSON,XML
}
