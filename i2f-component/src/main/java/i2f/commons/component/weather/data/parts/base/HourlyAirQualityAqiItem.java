package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HourlyAirQualityAqiItem {
    private String datetime;
    private AirQualityAqi value;
}
