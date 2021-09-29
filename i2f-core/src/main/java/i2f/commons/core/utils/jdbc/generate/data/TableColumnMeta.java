package i2f.commons.core.utils.jdbc.generate.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/8/25
 */
@Data
@NoArgsConstructor
public class TableColumnMeta {
    private String colName;
    private int colType;
    private String colTypeName;
    private int colDisplaySize;
    private String javaTypeString;
    private Class javaType;

}
