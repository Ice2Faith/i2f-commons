package i2f.commons.component.api.constellation.data;

import i2f.commons.component.api.constellation.data.detail.MimaConstellationData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class YearConstellationRespData {
    private String date;
    private String name;
    private String year;
    private MimaConstellationData mima;
    private List<String> career;
    private List<String> love;
    private List<String> health;
    private List<String> finance;
    private String luckeyStone;
    private String future;
    private String resultcode;
    private int error_code;
}
