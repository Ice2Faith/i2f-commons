package i2f.commons.component.api.jock.data;

import i2f.commons.component.api.jock.data.detail.JockRespResultData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JockRespData {
    private int error_code;
    private String reason;
    private JockRespResultData result;
}
