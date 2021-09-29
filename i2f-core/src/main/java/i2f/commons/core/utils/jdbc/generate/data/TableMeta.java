package i2f.commons.core.utils.jdbc.generate.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/8/25
 */
@Data
@NoArgsConstructor
public class TableMeta {
    private String schema;
    private String tableName;
    private List<TableColumnMeta> columns=new ArrayList<>();

}
