package i2f.commons.component.api.idioms.data;

import i2f.commons.component.api.idioms.data.detail.IdiomsResultData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IdiomsRespData {
    private int error_code;
    private String reason;
    private IdiomsResultData result;
}
