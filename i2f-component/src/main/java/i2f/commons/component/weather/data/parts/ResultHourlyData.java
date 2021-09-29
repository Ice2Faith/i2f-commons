package i2f.commons.component.weather.data.parts;

import i2f.commons.component.weather.data.parts.base.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResultHourlyData {
    private String status;
    private String description;
    private HourlyAirQuality air_quality;
    private List<DateTimeDblValue> cloudrate;
    private List<DateTimeDblValue> dswrf;
    private List<DateTimeDblValue> humidity;
    private List<DateTimeIntValue> precipitation;
    private List<DateTimeDblValue> pressure;
    private List<DateTimeStrValue> skycon;
    private List<DateTimeDblValue> temperature;
    private List<DateTimeDblValue> visibility;
    private List<HourlyWindItem> wind;
}
