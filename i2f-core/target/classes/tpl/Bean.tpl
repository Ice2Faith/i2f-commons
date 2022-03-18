package ${ctx.basePackage}.model;

import java.lang.*;
import java.util.*;
import java.math.*;
import java.io.Serializable;
import java.sql.*;
import lombok.*;

/**
 * @author ${ctx.author}
 * @date ${now@DateUtil.format}
 * @desc ${ctx.meta.tableName@GenerateContext.castTableName}Bean
 *       for table : ${ctx.meta.tableName}
 */
@Data
@NoArgsConstructor
public class ${ctx.meta.tableName@GenerateContext.castTableName}Bean implements Serializable {
    private static final long serialVersionUID = 1L;
    #{[for,ctx.meta.columns],separator="
    ",template="private ${_item.javaType@getSimpleName} ${_item.colName@GenerateContext.castColumnName};"}
}
