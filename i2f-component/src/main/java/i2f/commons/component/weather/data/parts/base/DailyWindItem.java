package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyWindItem {
    private String date;
    private RealtimeWind min;
    private RealtimeWind max;
    private RealtimeWind avg;
}
