package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HourlyWindItem {
    private String datetime;
    private Double direction;
    private Double speed;
}
