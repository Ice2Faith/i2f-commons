package i2f.commons.component.restapi.data.tips;

import i2f.commons.component.restapi.data.tips.detail.InputTipDetailItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TipsRespData {
    private int status;//返回状态
    private String info;//返回的状态信息
    private int count;//返回结果总数目
    private List<InputTipDetailItem> tips;//建议提示列表
}
