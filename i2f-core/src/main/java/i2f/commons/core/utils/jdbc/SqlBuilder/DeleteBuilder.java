package i2f.commons.core.utils.jdbc.SqlBuilder;


import i2f.commons.core.utils.jdbc.core.PreparedStatementBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteBuilder {
    private StringBuilder builder=new StringBuilder();
    private List<Object> params=new ArrayList<>();

    /**
     * where条件允许为空，则表示没有删除条件，这个你自己调用需要注意，否则造成删除全表内容概不负责
     * @param tableName
     * @param where
     * @param whereParams
     */
    public DeleteBuilder(String tableName,String where,Object ... whereParams){
        builder.append("DELETE FROM ");
        builder.append(tableName);
        if(where!=null){
            builder.append(where);
            for(Object item : whereParams){
                params.add(item);
            }
        }
    }
    public String build(){
        return builder.toString();
    }
    public PreparedStatement buildStat(Connection conn) throws SQLException {
        return PreparedStatementBuilder.makeByList(conn,build(),params);
    }
}
