package i2f.commons.component.api.idioms.data;

import i2f.commons.component.api.idioms.data.detail.IdiomsNextResultData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IdiomsNextRespData {
    private Integer error_code;
    private String reason;
    private IdiomsNextResultData result;
}
