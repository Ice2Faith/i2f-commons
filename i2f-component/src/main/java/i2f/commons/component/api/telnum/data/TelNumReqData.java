package i2f.commons.component.api.telnum.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelNumReqData {
    public static final String URL="http://apis.juhe.cn/mobile/get";
    public static final String METHOD="GET";
    private String key;
    private String phone;	//需要查询的手机号码或手机号码前7位
    private String dtype; //返回数据的格式,xml或json，默认json
}
