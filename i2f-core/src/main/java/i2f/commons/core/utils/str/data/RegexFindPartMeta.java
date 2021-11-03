package i2f.commons.core.utils.str.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/11/3
 */
@Data
@NoArgsConstructor
public class RegexFindPartMeta {
    public String part; //字符串部分
    public boolean isMatch; //指示是否是匹配的项
}
