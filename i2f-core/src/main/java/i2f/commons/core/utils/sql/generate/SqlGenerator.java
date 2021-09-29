package i2f.commons.core.utils.sql.generate;


import i2f.commons.core.utils.sql.generate.core.SqlBase;
import i2f.commons.core.utils.sql.generate.core.SqlPart;

import java.util.Map;

public class SqlGenerator {
    public static String update(String tableName, Map<String,Object> kvs,String wherePart){
        return SqlPart.comb(SqlBase.update(SqlBase.genKvOpe(tableName,"set",SqlPart.updEqus(kvs)))," ",SqlPart.where(wherePart));
    }
    public static String insert(String tableName,Map<String,Object> kvs){
        String[] colsVals=SqlPart.colsVals(kvs);
        return SqlPart.comb(SqlBase.insert(SqlBase.into(SqlBase.genKv(tableName,SqlBase.genBracket(colsVals[0]))))," ",SqlBase.values(colsVals[1]));
    }
    public static String delete(String tableName,String wherePart){
        return SqlPart.comb(SqlBase.delete(SqlBase.form(tableName))," ",SqlPart.where(wherePart));
    }

    public static String select(String tableName,String[] cols,String wherePart){
        return SqlPart.comb(SqlPart.comb(SqlBase.select(SqlPart.cols(cols))," ",SqlBase.form(tableName))," ",SqlPart.where(wherePart));
    }

}
