package i2f.commons.component.api.idioms.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IdiomsReqData {
    public static final String URL="http://apis.juhe.cn/idioms/query";
    public static final String METHOD="GET";
    private String key;
    private String wd;
}
