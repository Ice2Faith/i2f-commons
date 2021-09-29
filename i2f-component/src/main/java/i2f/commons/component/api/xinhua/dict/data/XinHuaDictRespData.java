package i2f.commons.component.api.xinhua.dict.data;

import i2f.commons.component.api.xinhua.dict.data.detail.XinHuaDictDetailItem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class XinHuaDictRespData {
    private int error_code;
    private String reason;
    private XinHuaDictDetailItem result;
}
