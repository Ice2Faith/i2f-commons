package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RealtimeAirQuality {
    private AirQualityAqi aqi;
    private Double co;
    private AirQualityDescription description;
    private Double no2;
    private Double o3;
    private Double pm10;
    private Double pm25;
    private Double so2;
}
