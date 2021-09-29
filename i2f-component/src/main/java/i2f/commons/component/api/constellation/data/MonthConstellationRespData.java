package i2f.commons.component.api.constellation.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonthConstellationRespData {
    private String date;
    private String name;
    private String month;
    private String health;
    private String all;
    private String love;
    private String money;
    private String work;
    private String happyMagic;
    private String resultcode;
    private int error_code;
}
