package i2f.commons.core.utils.jdbc.generate;

import i2f.commons.core.utils.jdbc.generate.data.GenerateContext;

/**
 * @author ltb
 * @date 2021/9/28
 */
public interface ITableGenerator {
    String generate(GenerateContext ctx);
}
