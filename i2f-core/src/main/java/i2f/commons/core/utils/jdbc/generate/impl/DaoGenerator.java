package i2f.commons.core.utils.jdbc.generate.impl;

import i2f.commons.core.utils.data.DateUtil;
import i2f.commons.core.utils.jdbc.generate.ITableGenerator;
import i2f.commons.core.utils.jdbc.generate.data.GenerateContext;
import i2f.commons.core.utils.jdbc.generate.data.TableMeta;
import i2f.commons.core.utils.str.AppendUtil;

import java.util.Date;

/**
 * @author ltb
 * @date 2021/9/28
 */
public class DaoGenerator implements ITableGenerator {
    @Override
    public String generate(GenerateContext ctx) {
        TableMeta meta= ctx.meta;
        AppendUtil.AppendBuilder builder=AppendUtil.builder()
                .addsLine("package ",ctx.basePackage,".dao;")
                .addsLine("")
                .addsLine("import java.lang.*;")
                .addsLine("import java.util.*;")
                .addsLine("import java.sql.*;")
                .addsLine("import org.apache.ibatis.annotations.*;")
                .addsLine("import ",ctx.basePackage,".model.",ctx.castTableName(meta.getTableName()),"Bean;")
                .addsLine("")
                .addsLine("/**")
                .addsLine(" * @author ",ctx.author)
                .addsLine(" * @date ", DateUtil.format(new Date()))
                .addsLine(" */")
                .addsLine("public interface ", ctx.castTableName(meta.getTableName()),"Dao"," {");

        builder.addsLine("\tList<",ctx.castTableName(meta.getTableName()),"Bean> queryList(@Param(\"bean\") ",ctx.castTableName(meta.getTableName()),"Bean bean);")
                .addsLine("\tint insertList(@Param(\"list\") List<",ctx.castTableName(meta.getTableName()),"Bean> list);")
                .addsLine("\tint updateOne(@Param(\"bean\") ",ctx.castTableName(meta.getTableName()),"Bean bean);")
                .addsLine("\tint deleteOne(@Param(\"bean\") ",ctx.castTableName(meta.getTableName()),"Bean bean);");

        builder.addsLine("");
        builder.addsLine("}");

        return builder.done();
    }

}
