package i2f.commons.component.api.telnum.data;

import i2f.commons.component.api.telnum.data.detail.TelNumResultData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelNumRespData {
    private int error_code;
    private String resultcode;
    private String reason;
    private TelNumResultData result;
}
