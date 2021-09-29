package i2f.commons.core.utils.pkg.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/9/23
 */
@Data
@NoArgsConstructor
public class ClassMetaData {
    private String className;
    private Class clazz;
    private String location;
}
