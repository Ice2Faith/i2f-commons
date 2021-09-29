package i2f.commons.component.api.telnum.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelNumResultData {
    private String province;
    private String city;
    private String areacode;
    private String zip;
    private String company;
    private String card;
}
