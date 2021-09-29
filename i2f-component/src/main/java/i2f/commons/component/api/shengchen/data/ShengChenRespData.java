package i2f.commons.component.api.shengchen.data;

import i2f.commons.component.api.shengchen.data.detail.ShengChenResultData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShengChenRespData {
    private int error_code;
    private String reason;
    private ShengChenResultData result;
}
