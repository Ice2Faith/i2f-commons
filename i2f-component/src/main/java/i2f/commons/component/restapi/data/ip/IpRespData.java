package i2f.commons.component.restapi.data.ip;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IpRespData {
    private int status;//返回结果状态值,0失败，1成功
    private String info;//返回状态说明
    private int infocode;//状态码,10000代表正确
    private String province;//省份名称
    private String city;//城市名称
    private String adcode;//城市的adcode编码
    private String rectangle;//所在城市矩形区域范围
}
