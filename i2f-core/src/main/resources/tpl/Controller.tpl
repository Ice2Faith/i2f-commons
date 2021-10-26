package ${ctx.basePackage}.controller;

import java.lang.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.*;
import ${ctx.basePackage}.model.${ctx.meta.tableName@GenerateContext.castTableName}Bean;
import ${ctx.basePackage}.service.I${ctx.meta.tableName@GenerateContext.castTableName}Service;
import lombok.*;

/**
 * @author ${ctx.author}
 * @date ${now@DateUtil.format}
 * @desc ${ctx.meta.tableName@GenerateContext.castTableName}Bean
 *       for table : ${ctx.meta.tableName}
 */
@RestController
@RequestMapping("${ctx.meta.tableName@GenerateContext.castTableName}")
public class ${ctx.meta.tableName@GenerateContext.castTableName}Controller {

    @Autowired
    private I${ctx.meta.tableName@GenerateContext.castTableName}Service beanService;

    @RequestMapping("queryList")
    public List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> queryList(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean){
        List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> list=beanService.queryList(bean);
        return list;
    }

    @RequestMapping("insert")
    public boolean insert(List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> list){
        boolean success=beanService.insert(list);
        return success;
    }

    @RequestMapping("update")
    public boolean update(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean){
        boolean success=beanService.update(bean);
        return success;
    }

    @RequestMapping("delete")
    public boolean delete(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean){
        boolean success=beanService.delete(bean);
        return success;
    }
}
