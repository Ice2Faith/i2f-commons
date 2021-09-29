package i2f.commons.component.kuaidi100.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Kuaidi100ReqData {
    public static final String URL="http://www.kuaidi100.com/query";
    public static final String METHOD="GET";
    private String type;
    private String postid;
    /**
     * 快递公司编码:
     * 申通="shentong"
     * EMS="ems"
     * 顺丰="shunfeng"
     * 圆通="yuantong"
     * 中通="zhongtong"
     * 韵达="yunda"
     * 天天="tiantian"
     * 汇通="huitongkuaidi"
     * 全峰="quanfengkuaidi"
     * 德邦="debangwuliu"
     * 宅急送="zhaijisong"
     */
}
