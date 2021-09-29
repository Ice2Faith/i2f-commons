package i2f.commons.component.weather.data.parts;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResultMinutelyData {
    private String status;
    private String datasource;
    private String description;
    private List<Double> precipitation;
    private List<Double> precipitation_2h;
    private List<Double> probability;
}
