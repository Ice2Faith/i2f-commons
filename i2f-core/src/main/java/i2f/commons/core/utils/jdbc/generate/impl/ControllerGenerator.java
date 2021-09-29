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
public class ControllerGenerator implements ITableGenerator {

    @Override
    public String generate(GenerateContext ctx) {
        TableMeta meta=ctx.meta;
        AppendUtil.AppendBuilder builder=AppendUtil.builder()
                .addsLine("package ",ctx.basePackage,".controller;")
                .addsLine("")
                .addsLine("import java.lang.*;")
                .addsLine("import java.util.*;")
                .addsLine("import java.sql.*;")
                .addsLine("import org.springframework.beans.factory.annotation.Autowired;")
                .addsLine("import org.springframework.web.bind.annotation.*;")
                .addsLine("import org.springframework.stereotype.*;")
                .addsLine("import ",ctx.basePackage,".service.I",ctx.castTableName(meta.getTableName()),"Service;")
                .addsLine("import ",ctx.basePackage,".model.",ctx.castTableName(meta.getTableName()),"Bean;")
                .addsLine("")
                .addsLine("/**")
                .addsLine(" * @author ",ctx.author)
                .addsLine(" * @date ", DateUtil.format(new Date()))
                .addsLine(" */")
                .addsLine("@RestController")
                .addsLine("@RequestMapping(\"",ctx.castTableName(meta.getTableName()),"\")")
                .addsLine("public class ", ctx.castTableName(meta.getTableName()),"Controller {")
                .addsLine("\t@Autowired")
                .addsLine("\tprivate I",ctx.castTableName(meta.getTableName()),"Service"," beanService;");

        builder.addsLine("\t@RequestMapping(\"queryList\")")
                .addsLine("\tpublic List<",ctx.castTableName(meta.getTableName()),"Bean> queryList(",ctx.castTableName(meta.getTableName()),"Bean bean) {")
                .addsLine("\t\tList<",ctx.castTableName(meta.getTableName()),"Bean> list=beanService.queryList(bean);")
                .addsLine("\t\treturn list;")
                .addsLine("\t}")
                .addsLine("\t@RequestMapping(\"insert\")")
                .addsLine("\tpublic boolean insert( List<",ctx.castTableName(meta.getTableName()),"Bean> list) {")
                .addsLine("\t\tboolean success=beanService.insert(list);")
                .addsLine("\t\treturn success;")
                .addsLine("\t}")
                .addsLine("\t@RequestMapping(\"update\")")
                .addsLine("\tpublic boolean update( ",ctx.castTableName(meta.getTableName()),"Bean bean) {")
                .addsLine("\t\tboolean success=beanService.update(bean);")
                .addsLine("\t\treturn success;")
                .addsLine("\t}")
                .addsLine("\t@RequestMapping(\"delete\")")
                .addsLine("\tpublic boolean delete( ",ctx.castTableName(meta.getTableName()),"Bean bean) {")
                .addsLine("\t\tboolean success=beanService.delete(bean);")
                .addsLine("\t\treturn success;")
                .addsLine("\t}");

        builder.addsLine("");
        builder.addsLine("}");

        return builder.done();

    }

}
