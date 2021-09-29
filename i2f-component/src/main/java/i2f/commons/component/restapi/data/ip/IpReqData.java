package i2f.commons.component.restapi.data.ip;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IpReqData {
    public static final String URL="https://restapi.amap.com/v3/ip";
    public static final String METHOD="GET";
    private String key; //请求服务权限标识,必填
    private String ip; //ip地址,可选
    private String sig; //签名,可选
}
