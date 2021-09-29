package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HourlyAirQuality {
    private List<HourlyAirQualityAqiItem> aqi;
    private List<Object> pm25;
}
