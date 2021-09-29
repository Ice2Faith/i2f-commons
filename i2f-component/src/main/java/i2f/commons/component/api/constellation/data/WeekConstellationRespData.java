package i2f.commons.component.api.constellation.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeekConstellationRespData {
    private String name;
    private String weekth;
    private String date;
    private String health;
    private String job;
    private String love;
    private String money;
    private String work;
    private String resultcode;
    private int error_code;
}
