package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailySkyConItem {
    private String date;
    private String value;
}
