package i2f.commons.core.utils.jdbc.generate.impl;

import i2f.commons.core.utils.jdbc.generate.ITableGenerator;
import i2f.commons.core.utils.jdbc.generate.data.GenerateContext;
import i2f.commons.core.utils.jdbc.generate.data.TableColumnMeta;
import i2f.commons.core.utils.jdbc.generate.data.TableMeta;
import i2f.commons.core.utils.str.AppendUtil;

/**
 * @author ltb
 * @date 2021/9/29
 */
public class AxiosInterfaceGenerator implements ITableGenerator {
    @Override
    public String generate(GenerateContext ctx) {

        TableMeta meta=ctx.meta;
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        builder.addsLine("let reqForm={")
                .adds("\t");
        boolean isFirst=true;
        for(TableColumnMeta item : meta.getColumns()){
            if(!isFirst){
                builder.adds("\n\t,");
            }
            builder.adds(ctx.castColumnName(item.getColName()),":","''");
            isFirst=false;
        }
        builder.addsLine("\n};\n");

        builder.addsLine("this.$axios({")
                .addsLine("\turl:'",ctx.castTableName(meta.getTableName()),"/queryList',")
                .addsLine("\tmethod:'post',")
                .addsLine("\tdata:reqForm")
                .addsLine("}).then(resp=>{")
                .addsLine("\tlet list=resp;")
                .addsLine("\tconsole.log('list',list);")
                .addsLine("})");

        builder.addsLine("");

        builder.addsLine("this.$axios({")
                .addsLine("\turl:'",ctx.castTableName(meta.getTableName()),"/insert',")
                .addsLine("\tmethod:'post',")
                .addsLine("\tdata:[reqForm]")
                .addsLine("}).then(resp=>{")
                .addsLine("\tlet success=resp;")
                .addsLine("\tconsole.log('success',list);")
                .addsLine("})");

        builder.addsLine("");

        builder.addsLine("this.$axios({")
                .addsLine("\turl:'",ctx.castTableName(meta.getTableName()),"/update',")
                .addsLine("\tmethod:'post',")
                .addsLine("\tdata:reqForm")
                .addsLine("}).then(resp=>{")
                .addsLine("\tlet success=resp;")
                .addsLine("\tconsole.log('success',list);")
                .addsLine("})");

        builder.addsLine("");

        builder.addsLine("this.$axios({")
                .addsLine("\turl:'",ctx.castTableName(meta.getTableName()),"/delete',")
                .addsLine("\tmethod:'post',")
                .addsLine("\tdata:reqForm")
                .addsLine("}).then(resp=>{")
                .addsLine("\tlet success=resp;")
                .addsLine("\tconsole.log('success',list);")
                .addsLine("})");

        return builder.done();
    }
}
