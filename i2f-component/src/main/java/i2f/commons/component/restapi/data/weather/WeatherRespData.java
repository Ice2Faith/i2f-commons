package i2f.commons.component.restapi.data.weather;


import i2f.commons.component.restapi.data.weather.detail.WeatherForecastItem;
import i2f.commons.component.restapi.data.weather.detail.WeatherLiveItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WeatherRespData {
    private int status;//返回状态
    private int count;//返回结果总数目
    private String info;//返回的状态信息
    private int infocode;//返回状态说明,10000代表正确
    private List<WeatherLiveItem> lives;//实况天气数据信息
    private List<WeatherForecastItem> forecasts;//预报天气信息数据
}
