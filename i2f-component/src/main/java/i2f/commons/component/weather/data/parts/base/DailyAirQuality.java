package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DailyAirQuality {
    private List<DailyAirQualityAqiItem> aqi;
    private List<DailyDblMaxMinAvgItem> pm25;
}
