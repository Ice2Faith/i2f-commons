package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyDblMaxMinAvgItem {
    private String date;
    private Double min;
    private Double max;
    private Double avg;
}
