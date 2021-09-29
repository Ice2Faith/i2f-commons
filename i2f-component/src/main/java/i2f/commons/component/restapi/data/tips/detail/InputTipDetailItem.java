package i2f.commons.component.restapi.data.tips.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InputTipDetailItem {
    private String id;//返回数据ID
    private String name;//tip名称
    private String district;//所属区域
    private String adcode;//区域编码
    private String location;//tip中心点坐标
    private String address;//详细地址
}
