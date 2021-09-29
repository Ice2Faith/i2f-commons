package i2f.commons.component.api.hotvideo.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotVideoReqData {
    public static final String URL="http://apis.juhe.cn/fapig/douyin/billboard";
    public static final String METHOD="GET";
    private String key;
    private String type="hot_video";
    private int size;
}
