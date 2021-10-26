package ${ctx.basePackage}.service.impl;

import java.lang.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import org.springframework.stereotype.*;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ${ctx.basePackage}.model.${ctx.meta.tableName@GenerateContext.castTableName}Bean;
import ${ctx.basePackage}.dao.${ctx.meta.tableName@GenerateContext.castTableName}Dao;
import ${ctx.basePackage}.service.I${ctx.meta.tableName@GenerateContext.castTableName}Service;

/**
 * @author ${ctx.author}
 * @date ${now@DateUtil.format}
 * @desc ${ctx.meta.tableName@GenerateContext.castTableName}ServiceImpl
 *       for table : ${ctx.meta.tableName}
 */
@Service
public class ${ctx.meta.tableName@GenerateContext.castTableName}ServiceImpl implements I${ctx.meta.tableName@GenerateContext.castTableName}Service{
    private Logger logger= LoggerFactory.getLogger(${ctx.meta.tableName@GenerateContext.castTableName}ServiceImpl.class);

    @Resource
    private ${ctx.meta.tableName@GenerateContext.castTableName}Dao beanDao;

    @Override
    public List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> queryList(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean) {
        List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> list=beanDao.queryList(bean);
        return list;
    }

    @Override
    public boolean insert(List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> list) {
        int effecLine=beanDao.insertList(list);
        return effecLine>0;
    }

    @Override
    public boolean update(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean){
        int effecLine=beanDao.updateOne(bean);
        return effecLine>0;
    }

    @Override
    public boolean delete(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean){
        int effecLine=beanDao.deleteOne(bean);
        return effecLine>0;
    }
}
