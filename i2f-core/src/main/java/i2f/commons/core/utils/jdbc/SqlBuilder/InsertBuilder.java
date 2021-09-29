package i2f.commons.core.utils.jdbc.SqlBuilder;


import i2f.commons.core.utils.jdbc.core.PreparedStatementBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InsertBuilder {
    private StringBuilder builder=new StringBuilder();
    private List<Object> params=new ArrayList<>();

    public InsertBuilder(String tableName, Map<String,Object> maps,String spaceHolder){
        StringBuilder colBuilder=new StringBuilder();
        StringBuilder valBuilder=new StringBuilder();

        boolean isFirst=true;
        for(String col : maps.keySet()){
            Object val=maps.get(col);
            if(val==null){
                continue;
            }
            if(val instanceof String){
                if("".equals(val)){
                    continue;
                }
            }
            params.add(val);
            if(isFirst){
                isFirst=false;
            }else{
                colBuilder.append(",");
                valBuilder.append(",");
            }
            colBuilder.append(col);
            valBuilder.append(spaceHolder);
        }


        builder.append("INSERT INTO ");
        builder.append(tableName);
        builder.append("(");
        builder.append(colBuilder.toString());
        builder.append(")");
        builder.append(" VALUES(");
        builder.append(valBuilder.toString());
        builder.append(")");
    }
    public InsertBuilder(String tableName,String spaceHolder,String[] cols,Object ... vals){
        StringBuilder colBuilder=new StringBuilder();
        StringBuilder valBuilder=new StringBuilder();

        for(int i=0;i<cols.length;i++){
            Object val=vals[i];
            if(val==null){
                continue;
            }
            if(val instanceof String){
                if("".equals(val)){
                    continue;
                }
            }
            params.add(val);
            if(i!=0){
                colBuilder.append(",");
                valBuilder.append(",");
            }
            colBuilder.append(cols[i]);
            valBuilder.append(spaceHolder);
        }


        builder.append("INSERT INTO ");
        builder.append(tableName);
        builder.append("(");
        builder.append(colBuilder.toString());
        builder.append(")");
        builder.append(" VALUES(");
        builder.append(valBuilder.toString());
        builder.append(")");
    }
    public String build(){
        return builder.toString();
    }
    public PreparedStatement buildStat(Connection conn) throws SQLException {
        return PreparedStatementBuilder.makeByList(conn,build(),params);
    }
}
