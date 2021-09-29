package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DailyLifeIndex {
    private List<DailyLifeIndexItem> carWashing;
    private List<DailyLifeIndexItem> coldRisk;
    private List<DailyLifeIndexItem> comfort;
    private List<DailyLifeIndexItem> dressing;
    private List<DailyLifeIndexItem> ultraviolet;
}
