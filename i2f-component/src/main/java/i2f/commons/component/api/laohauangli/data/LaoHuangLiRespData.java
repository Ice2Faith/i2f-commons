package i2f.commons.component.api.laohauangli.data;

import i2f.commons.component.api.laohauangli.data.detail.LaoHuangLiResultData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LaoHuangLiRespData {
    private int error_code;
    private String reason;
    private LaoHuangLiResultData result;
}
