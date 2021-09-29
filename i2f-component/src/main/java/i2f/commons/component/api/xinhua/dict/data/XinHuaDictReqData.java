package i2f.commons.component.api.xinhua.dict.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class XinHuaDictReqData {
    public static final String URL="http://v.juhe.cn/xhzd/query";
    public static final String METHOD="GET";
    private String key;//请求KEY
    private char word;//填写需要查询的汉字，UTF8 urlencode编码
    private String dtype;//	返回数据的格式,xml或json，默认json
}
