package i2f.commons.core.utils.str.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Data
@NoArgsConstructor
public class RegexMatchItem {
    public String srcStr;
    public String regexStr;
    public String matchStr;
    public int idxStart;
    public int idxEnd;
}
