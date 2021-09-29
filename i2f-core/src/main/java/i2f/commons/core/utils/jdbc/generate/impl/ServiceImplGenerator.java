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
public class ServiceImplGenerator implements ITableGenerator {
    @Override
    public String generate(GenerateContext ctx) {
        TableMeta meta=ctx.meta;
        AppendUtil.AppendBuilder builder=AppendUtil.builder()
                .addsLine("package ",ctx.basePackage,".service.impl;")
                .addsLine("")
                .addsLine("import java.lang.*;")
                .addsLine("import java.util.*;")
                .addsLine("import java.sql.*;")
                .addsLine("import org.springframework.stereotype.*;")
                .addsLine("import javax.annotation.Resource;")
                .addsLine("import org.slf4j.Logger;")
                .addsLine("import org.slf4j.LoggerFactory;")
                .addsLine("import ",ctx.basePackage,".service.I",ctx.castTableName(meta.getTableName()),"Service;")
                .addsLine("import ",ctx.basePackage,".model.",ctx.castTableName(meta.getTableName()),"Bean;")
                .addsLine("import ",ctx.basePackage,".dao.",ctx.castTableName(meta.getTableName()),"Dao;")
                .addsLine("")
                .addsLine("/**")
                .addsLine(" * @author ",ctx.author)
                .addsLine(" * @date ", DateUtil.format(new Date()))
                .addsLine(" */")
                .addsLine("@Service")
                .addsLine("public class ", ctx.castTableName(meta.getTableName()),"ServiceImpl","  implements I",ctx.castTableName(meta.getTableName()),"Service {")
                .addsLine("\tprivate Logger logger= LoggerFactory.getLogger(",ctx.castTableName(meta.getTableName()),"ServiceImpl.class);")
                .addsLine("\t@Resource")
                .addsLine("\tprivate ",ctx.castTableName(meta.getTableName()),"Dao"," beanDao;");

        builder.addsLine("\t@Override")
                .addsLine("\tpublic List<",ctx.castTableName(meta.getTableName()),"Bean> queryList(",ctx.castTableName(meta.getTableName()),"Bean bean) {")
                .addsLine("\t\tList<",ctx.castTableName(meta.getTableName()),"Bean> list=beanDao.queryList(bean);")
                .addsLine("\t\treturn list;")
                .addsLine("\t}")
                .addsLine("\tpublic boolean insert( List<",ctx.castTableName(meta.getTableName()),"Bean> list) {")
                .addsLine("\t\tint effecLine=beanDao.insertList(list);")
                .addsLine("\t\treturn effecLine>0;")
                .addsLine("\t}")
                .addsLine("\tpublic boolean update( ",ctx.castTableName(meta.getTableName()),"Bean bean) {")
                .addsLine("\t\tint effecLine=beanDao.updateOne(bean);")
                .addsLine("\t\treturn effecLine>0;")
                .addsLine("\t}")
                .addsLine("\tpublic boolean delete( ",ctx.castTableName(meta.getTableName()),"Bean bean) {")
                .addsLine("\t\tint effecLine=beanDao.deleteOne(bean);")
                .addsLine("\t\treturn effecLine>0;")
                .addsLine("\t}");

        builder.addsLine("");
        builder.addsLine("}");

        return builder.done();
    }

}
