package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyAstroItem {
    private String date;
    private AstroTime sunrise;
    private AstroTime sunset;
}
