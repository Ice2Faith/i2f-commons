package i2f.commons.component.restapi.data.ip;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ip2ReqData {
    public static final String URL="https://restapi.amap.com/v5/ip";
    public static final String METHOD="GET";
    private String key; //请求服务权限标识,必填
    private String type;//IP类型，4：Ipv4 6 ipv6 必填
    private String ip; //ip地址,必填
    private String sig; //签名,可选
}
