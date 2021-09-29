package i2f.commons.component.weather.data.parts.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RealtimePrecipitationLocal {
    private String datasource;
    private Double intensity;
    private String status;
}
