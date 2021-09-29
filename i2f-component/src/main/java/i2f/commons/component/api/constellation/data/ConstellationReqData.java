package i2f.commons.component.api.constellation.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConstellationReqData {
    public static final String URL="http://web.juhe.cn:8080/constellation/getAll";
    public static final String METHOD="GET";
    private String key;
    private String consName;//	星座名称，如:双鱼座
    private String type;//运势类型：today,tomorrow,week,month,year
}
