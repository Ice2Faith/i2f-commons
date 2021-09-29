package i2f.commons.component.weather.data;

import i2f.commons.component.weather.data.detail.CommonWeatherResultData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommonWeatherRespData {
    private String api_status;
    private String api_version;
    private String lang;
    private List<Double> location;
    private Long server_time;
    private String status;
    private String timezone;
    private Long tzshift;
    private String unit;
    private CommonWeatherResultData result;
}
