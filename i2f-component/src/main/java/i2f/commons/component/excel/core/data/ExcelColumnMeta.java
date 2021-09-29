package i2f.commons.component.excel.core.data;

import i2f.commons.component.excel.core.annotation.ExcelColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

/**
 * @author ltb
 * @date 2021/9/17
 */
@Data
@NoArgsConstructor
public class ExcelColumnMeta {
    private ExcelColumn annotation;
    private Field field;
}
