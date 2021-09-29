package i2f.commons.component.api.constellation.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DayConstellationRespData {
    private String date;
    private String name;
    private String QFriend;
    private String color;
    private String datetime;
    private String health;
    private String love;
    private String work;
    private String money;
    private int number;
    private String summary;
    private String all;
    private String resultcode;
    private int error_code;
}
