package i2f.commons.component.restapi.data.ip;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ip2RespData {
    private int status;//返回结果状态值,0失败，1成功
    private String info;//返回状态说明
    private int infocode;//状态码,10000代表正确
    private String country;//国家
    private String province;//省份名称
    private String city;//城市名称
    private String district;//区
    private String isp;//运营商，移动、联通、电信
    private String location;//精度在前维度在后，X,Y
    private String ip;//提交的IP
}
