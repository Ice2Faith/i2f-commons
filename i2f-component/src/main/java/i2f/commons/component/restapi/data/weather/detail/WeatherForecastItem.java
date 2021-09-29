package i2f.commons.component.restapi.data.weather.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WeatherForecastItem {
    private String city;//城市名称
    private String adcode;//城市编码
    private String province;//省份名称
    private String reporttime;//预报发布时间
    private List<WeatherForecastDetailItem> casts;
}
