package i2f.commons.component.weather.data.parts;


import i2f.commons.component.weather.data.parts.base.RealtimeAirQuality;
import i2f.commons.component.weather.data.parts.base.RealtimeLifeIndex;
import i2f.commons.component.weather.data.parts.base.RealtimePrecipitation;
import i2f.commons.component.weather.data.parts.base.RealtimeWind;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultRealtimeData {
    private RealtimeAirQuality air_quality;
    private Double apparent_temperature;
    private Double cloudrate;
    private Double dswrf;
    private Double humidity;
    private RealtimeLifeIndex life_index;
    private RealtimePrecipitation precipitation;
    private Double pressure;
    private String skycon;
    private String status;
    private Double temperature;
    private Double visibility;
    private RealtimeWind wind;
}
