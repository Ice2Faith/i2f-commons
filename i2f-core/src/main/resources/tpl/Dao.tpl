package ${ctx.basePackage}.dao;

import java.lang.*;
import java.util.*;
import java.io.Serializable;
import java.sql.*;
import org.apache.ibatis.annotations.*;
import lombok.*;
import ${ctx.basePackage}.model.${ctx.meta.tableName@GenerateContext.castTableName}Bean;


/**
 * @author ${ctx.author}
 * @date ${now@DateUtil.format}
 * @desc ${ctx.meta.tableName@GenerateContext.castTableName}Dao
 *       for table : ${ctx.meta.tableName}
 */
public interface ${ctx.meta.tableName@GenerateContext.castTableName}Dao {
    List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> queryList(@Param("bean") ${ctx.meta.tableName@GenerateContext.castTableName}Bean bean);
    int insertList(@Param("list") List<${ctx.meta.tableName@GenerateContext.castTableName}Bean> list);
    int updateOne(@Param("bean") ${ctx.meta.tableName@GenerateContext.castTableName}Bean bean);
    int deleteOne(@Param("bean") ${ctx.meta.tableName@GenerateContext.castTableName}Bean bean);
}
