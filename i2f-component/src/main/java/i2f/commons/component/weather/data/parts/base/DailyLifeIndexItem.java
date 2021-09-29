package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyLifeIndexItem {
    private String date;
    private String desc;
    private String index;
}
