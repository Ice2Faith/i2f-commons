package i2f.commons.component.api.idioms.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IdiomsNextReqData {
    public static final String URL="http://apis.juhe.cn/idiomJie/query";
    public static final String METHOD="GET";
    private String key;
    private String wd;
    private Integer size;
    private String is_rand;//随机返回，1是 2否
}
