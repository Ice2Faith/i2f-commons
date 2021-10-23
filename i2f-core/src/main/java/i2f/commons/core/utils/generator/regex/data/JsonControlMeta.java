package i2f.commons.core.utils.generator.regex.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author ltb
 * @date 2021/10/20
 */
@Data
@NoArgsConstructor
public class JsonControlMeta {
    public String action;
    public String routeExpression;
    public Map<String,String> parameters;
}
