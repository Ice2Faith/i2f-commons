package i2f.commons.component.weather.data.parts;

import i2f.commons.component.weather.data.parts.base.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResultDailyData {
    private String status;
    private String forecast_keypoint;
    private DailyAirQuality air_quality;
    private List<DailyAstroItem>  astro;
    private List<DailyDblMaxMinAvgItem> cloudrate;
    private List<DailyDblMaxMinAvgItem> dswrf;
    private List<DailyDblMaxMinAvgItem> humidity;
    private DailyLifeIndex life_index;
    private List<DailyDblMaxMinAvgItem> precipitation;
    private List<DailyDblMaxMinAvgItem> pressure;
    private List<DailySkyConItem> skycon;
    private List<DailySkyConItem> skycon_08h_20h;
    private List<DailySkyConItem> skycon_20h_32h;
    private List<DailyDblMaxMinAvgItem> temperature;
    private List<DailyDblMaxMinAvgItem> visibility;
    private List<DailyWindItem> wind;
}
