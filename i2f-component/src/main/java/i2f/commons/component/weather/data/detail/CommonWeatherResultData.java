package i2f.commons.component.weather.data.detail;


import i2f.commons.component.weather.data.parts.ResultDailyData;
import i2f.commons.component.weather.data.parts.ResultHourlyData;
import i2f.commons.component.weather.data.parts.ResultMinutelyData;
import i2f.commons.component.weather.data.parts.ResultRealtimeData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonWeatherResultData {
    private String forecast_keypoint;
    private Long primary;
    private ResultDailyData daily;
    private ResultHourlyData hourly;
    private ResultMinutelyData minutely;
    private ResultRealtimeData realtime;
}
