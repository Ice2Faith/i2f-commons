package i2f.commons.core.utils.jdbc.generate.impl;

import i2f.commons.core.utils.jdbc.generate.ITableGenerator;
import i2f.commons.core.utils.jdbc.generate.data.GenerateContext;
import i2f.commons.core.utils.jdbc.generate.data.TableColumnMeta;
import i2f.commons.core.utils.jdbc.generate.data.TableMeta;
import i2f.commons.core.utils.str.AppendUtil;

/**
 * @author ltb
 * @date 2021/9/28
 */
public class MapperGenerator implements ITableGenerator {
    @Override
    public String generate(GenerateContext ctx) {
        TableMeta meta=ctx.meta;
        AppendUtil.AppendBuilder builder=AppendUtil.builder()
                .addsLine("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
                .addsLine("<!DOCTYPE mapper")
                .addsLine("    PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"")
                .addsLine("            \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">")
                .addsLine("<mapper namespace=\"",ctx.basePackage,".dao.",ctx.castTableName(meta.getTableName()),"Dao","\">");

        builder.addsLine("")
                .addsLine("\t<resultMap id=\"beanMap\" type=\"",ctx.basePackage,".model.",ctx.castTableName(meta.getTableName()),"Bean","\">");
        for(TableColumnMeta col : meta.getColumns()){
            builder.addsLine("\t\t<result property=\"",ctx.castColumnName(col.getColName()),"\" column=\"",col.getColName(),"\"/>");
        }
        builder.addsLine("\t</resultMap>");

        builder.addsLine("")
                .addsLine("\t<select id=\"queryList\" resultMap=\"beanMap\">")
                .addsLine("\t\tselect ");
        boolean isFirst=true;
        for(TableColumnMeta col : meta.getColumns()){
            String dot="";
            if(!isFirst){
                dot=",";
            }
            builder.addsLine("\t\t\t",dot,"t1.",col.getColName());
            isFirst=false;
        }
        builder.addsLine("\t\tfrom ", meta.getTableName()," t1")
                .addsLine("")
                .addsLine("\t\t<where>")
                .addsLine("");
        for(TableColumnMeta col : meta.getColumns()){
            if(String.class.equals(col.getJavaType())) {
                builder.addsLine("\t\t\t<if test=\"bean.", ctx.castColumnName(col.getColName()), "!=null and bean.", ctx.castColumnName(col.getColName()), "!=''\">")
                        .addsLine("\t\t\t\tt1.", col.getColName(), "=#{bean.", ctx.castColumnName(col.getColName()), "}")
                        .addsLine("\t\t\t</if>");
            }else{
                builder.addsLine("\t\t\t<if test=\"bean.", ctx.castColumnName(col.getColName()), "!=null\">")
                        .addsLine("\t\t\t\tt1.", col.getColName(), "=#{bean.", ctx.castColumnName(col.getColName()), "}")
                        .addsLine("\t\t\t</if>");
            }
        }
        builder.addsLine("\t\t</where>")
                .addsLine("\t</select>");


        builder.addsLine("")
                .addsLine("\t<insert id=\"insertList\">")
                .addsLine("\t\tinsert into ")
                .adds("\t\t\t",meta.getTableName());
        builder.add(" (");
        isFirst=true;
        for(TableColumnMeta col : meta.getColumns()){
            if(!isFirst){
                builder.add(" , ");
            }
            builder.add(col.getColName());
            isFirst=false;
        }
        builder.addsLine(" ) ");
        builder.addsLine("\t\tvalues");
        builder.addsLine("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
        builder.adds("\t\t\t(");
        isFirst=true;
        for(TableColumnMeta col : meta.getColumns()){
            if(!isFirst){
                builder.add(" , ");
            }
            builder.adds("#{item.",ctx.castColumnName(col.getColName()),"}");
            isFirst=false;
        }
        builder.addsLine(")");
        builder.addsLine("\t\t</foreach>");

        builder.addsLine("\t</insert>");

        builder.addsLine("")
                .addsLine("\t<update id=\"updateOne\">")
                .addsLine("\t\tupdate ",meta.getTableName())
                .addsLine("\t\tset")
                .addsLine("\t\t<trim suffixOverrides=\",\">");

        for(TableColumnMeta col : meta.getColumns()){
            if(String.class.equals(col.getJavaType())){
                builder.addsLine("\t\t\t<if test=\"bean.",ctx.castColumnName(col.getColName()),"!=null and bean.",ctx.castColumnName(col.getColName()),"!=''\">");
            }else{
                builder.addsLine("\t\t\t<if test=\"bean.",ctx.castColumnName(col.getColName()),"!=null\">");
            }
            builder.addsLine("\t\t\t\t",col.getColName(),"=","#{bean.",ctx.castColumnName(col.getColName()),"}");
            builder.addsLine("\t\t\t</if>");
        }

        builder.addsLine("\t\t</trim>")
                .addsLine("\t\twhere")
                .addsLine("\t\t\t1!=1")
                .addsLine("\t</update>");

        builder.addsLine("")
                .addsLine("\t<delete id=\"deleteOne\">")
                .addsLine("\t\tdelete from ",meta.getTableName())
                .addsLine("\t\twhere")
                .addsLine("\t\t\t1!=1")
                .addsLine("\t</delete>");

        builder.addsLine("</mapper>");

        return builder.done();
    }

}
