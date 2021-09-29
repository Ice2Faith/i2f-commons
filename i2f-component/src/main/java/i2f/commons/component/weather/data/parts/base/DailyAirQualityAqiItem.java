package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyAirQualityAqiItem {
    private String date;
    private AirQualityAqi min;
    private AirQualityAqi max;
    private AirQualityAqi avg;
}
