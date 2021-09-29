package i2f.commons.core.utils.jdbc.generate.impl;

import i2f.commons.core.utils.data.DateUtil;
import i2f.commons.core.utils.jdbc.generate.ITableGenerator;
import i2f.commons.core.utils.jdbc.generate.data.GenerateContext;
import i2f.commons.core.utils.jdbc.generate.data.TableColumnMeta;
import i2f.commons.core.utils.jdbc.generate.data.TableMeta;
import i2f.commons.core.utils.str.AppendUtil;

import java.util.Date;

/**
 * @author ltb
 * @date 2021/9/28
 */
public class BeanGenerator implements ITableGenerator {

    @Override
    public String generate(GenerateContext ctx) {
        TableMeta meta=ctx.meta;
        AppendUtil.AppendBuilder builder=AppendUtil.builder()
                .addsLine("package ",ctx.basePackage,".model;")
                .addsLine("")
                .addsLine("import java.lang.*;")
                .addsLine("import java.util.*;")
                .addsLine("import java.io.Serializable;")
                .addsLine("import java.sql.*;")
                .addsLine("import lombok.*;")
                .addsLine("")
                .addsLine("/**")
                .addsLine(" * @author ",ctx.author)
                .addsLine(" * @date ", DateUtil.format(new Date()))
                .addsLine(" */")
                .addsLine("@Data")
                .addsLine("@NoArgsConstructor")
                .addsLine("public class ", ctx.castTableName(meta.getTableName()),"Bean","  implements Serializable {")
                .addsLine("\t","private static final long serialVersionUID = 1L;");

        for(TableColumnMeta col : meta.getColumns()){
            builder.addsLine("\tprivate ",col.getJavaType().getSimpleName()," ",ctx.castColumnName(col.getColName()),";");
        }

        builder.addsLine("");
        builder.addsLine("}");

        return builder.done();
    }
}
