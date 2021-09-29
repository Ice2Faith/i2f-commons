package i2f.commons.core.utils.jdbc.SqlBuilder;


import i2f.commons.core.utils.jdbc.core.PreparedStatementBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 注意，由于允许where条件为空，因此，你可能需要根据你的情况，指明具体的构造重载，对于where为null的地方
 * 例如：
 * UpdateBuilder updateBuilder=new UpdateBuilder("Student","?",
 *                     (String) null,null,
 *                     new String[]{"name","age","sex"},
 *                     "张三",20,"man");
 * 将第三个参数强制指明为String，避免Java无法解析具体的构造参数列表
 */
public class UpdateBuilder {
    private StringBuilder builder=new StringBuilder();
    private List<Object> params=new ArrayList<>();

    /**
     * where条件允许为空，则无条件进行全表更新
     * @param tableName
     * @param spaceHolder
     * @param maps
     * @param where
     * @param whereParams
     */
    public UpdateBuilder(String tableName, String spaceHolder, Map<String,Object> maps,String where,Object ... whereParams){
        StringBuilder colBuilder=new StringBuilder();

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
            }
            colBuilder.append(col);
            colBuilder.append("=");
            colBuilder.append(spaceHolder);
        }


        builder.append("UPDATE ");
        builder.append(tableName);
        builder.append(" SET ");
        builder.append(colBuilder.toString());
        if(where!=null){
            builder.append(" WHERE ");
            builder.append(where);
            for(Object item : whereParams){
                params.add(item);
            }
        }
    }

    /**
     * where条件允许为空，执行全表更新
     * @param tableName
     * @param spaceHolder
     * @param where
     * @param whereParams
     * @param cols
     * @param vals
     */
    public UpdateBuilder(String tableName,String spaceHolder,String where,Object[] whereParams,String[] cols,Object ... vals){
        StringBuilder colBuilder=new StringBuilder();

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
            }
            colBuilder.append(cols[i]);
            colBuilder.append("=");
            colBuilder.append(spaceHolder);
        }


        builder.append("UPDATE ");
        builder.append(tableName);
        builder.append(" SET ");
        builder.append(colBuilder.toString());
        if(where!=null){
            builder.append(" WHERE ");
            builder.append(where);
            for(Object item : whereParams){
                params.add(item);
            }
        }
    }

    /**
     * where条件允许为空，执行全表更新
     * setPart参数解释：
     * 表示你自己的set语句部分，这是更加灵活的表现
     * 示例：
     * String sql=new UpdateBuilder("Student",null,null,"age=age+?",1);
     * 这样就对全表执行了年龄加1的操作
     * 下面是等价的
     * new UpdateBuilder("Student",null,null,"age=age+1");
     * @param tableName
     * @param where
     * @param whereParams
     * @param setPart
     * @param setParams
     */
    public UpdateBuilder(String tableName,String where,Object[] whereParams,String setPart,Object ... setParams){
        builder.append("UPDATE ");
        builder.append(tableName);
        builder.append(" SET ");
        builder.append(setPart);
        for(Object item : setParams){
            params.add(item);
        }

        if(where!=null){
            builder.append(" WHERE ");
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
