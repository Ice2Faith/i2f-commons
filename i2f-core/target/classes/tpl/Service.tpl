package ${ctx.basePackage}.service;

import java.lang.*;
import java.util.*;
import java.math.*;
import java.io.Serializable;
import java.sql.*;
import ${ctx.basePackage}.model.${ctx.meta.tableName@GenerateContext.castTableName}Bean;

/**
 * @author ${ctx.author}
 * @date ${now@DateUtil.format}
 * @desc ${ctx.meta.tableName@GenerateContext.castTableName}Bean
 *       for table : ${ctx.meta.tableName}
 */
public interface I${ctx.meta.tableName@GenerateContext.castTableName}Service {
    List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> queryList(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean);
    boolean insert(List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> list);
    boolean update(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean);
    boolean delete(${ctx.meta.tableName@GenerateContext.castTableName}Bean bean);
}
