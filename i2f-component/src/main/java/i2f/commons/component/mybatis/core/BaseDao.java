package i2f.commons.component.mybatis.core;


import i2f.commons.core.utils.jdbc.wrapper.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseDao { // use : extends this class and copy mapper file to your project from jar:mapper
    List<Map<String,Object>> queryNative(@Param("sql") String sql);

    int insertNative(@Param("sql") String sql);

    int updateNative(@Param("sql") String sql);

    int deleteNative(@Param("sql") String sql);

    int executeNative(@Param("sql")String sql);

    List<Map<String,Object>> queryAll(@Param("tableName")String tableName);

    int emptyTable(@Param("tableName")String tableName);

    int dropTable(@Param("tableName")String tableName);

    long countTable(@Param("tableName")String tableName);

    List<Map<String,Object>> queryCommon(@Param("wrapper") QueryWrapper wrapper);

    long queryCountCommon(@Param("wrapper") QueryWrapper wrapper);

    int deleteCommon(@Param("wrapper") DeleteWrapper wrapper);

    int updateCommon(@Param("wrapper") UpdateWrapper wrapper);

    int insertCommon(@Param("wrapper") InsertWrapper wrapper);

    int insertCommonReturnId(@Param("wrapper") InsertWrapper wrapper);

    int insertCommonBatch(@Param("wrapper") InsertBatchWrapper wrapper);

    List<Map<String,Object>> queryCommonMySqlPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommOraclePage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonMariaDBPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonGbasePage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonOscarPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonXuGuPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonClickHousePage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonOceanBasePage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonPostgrePage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonPostgreSqlPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonH2Page(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonSqlLitePage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonHSqlPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonKingBaseEsPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonPhoenixPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommDmPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommGaussPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonOracle12cPage(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonDb2Page(@Param("wrapper") QueryWrapper wrapper);

    List<Map<String,Object>> queryCommonSQLServerPage(@Param("wrapper") QueryWrapper wrapper);

}
