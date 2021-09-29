package i2f.commons.core.utils.jdbc.SqlBuilder;


import i2f.commons.core.exception.CommonsException;
import i2f.commons.core.utils.jdbc.core.PreparedStatementBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectBuilder {
    private StringBuilder builder=new StringBuilder();
    private List<Object> params=new ArrayList<>();

    /**
     * 直接构造出一个全表查询语句，你可以通过添加add*()方法来添加约束
     * @param tableName
     * @param cols
     */
    public SelectBuilder(String tableName, String ... cols){
        builder.append("SELECT ");
        builder.append(getColsStr(cols));
        builder.append(" FROM ");
        builder.append(tableName);
    }

    /**
     * 直接返回创建的预处理SQL语句
     * @return
     */
    public String build(){
        return builder.toString();
    }

    /**
     * 根据传入的参数，和连接对象，直接返回PreparedStatement对象，以便直接执行和使用
     * @param conn
     * @return
     * @throws CommonsException
     */
    public PreparedStatement buildStat(Connection conn) throws SQLException {
        return PreparedStatementBuilder.makeByList(conn,build(),params);
    }
    private String getColsStr(String ... cols){
       StringBuilder colBuilder=new StringBuilder();
       for(int i=0;i<cols.length;i++){
           if(i!=0){
               colBuilder.append(",");
           }
           colBuilder.append(cols[i]);
       }
       return colBuilder.toString();
    }

    /**
     * 将是一个不带检查的直接拼接和生成Where条件句
     * @param where
     * @param whereParams
     * @return
     */
    public SelectBuilder addWhereRoot(String where, Object ... whereParams){
        builder.append(" WHERE ");
        builder.append(where);
        for(Object item : whereParams){
            params.add(item);
        }
        return this;
    }

    /**
     * 根据testPass的指示，判定是否需要拼接此条件
     * 如果testPass为真，则执行拼接
     * 否则，不执行拼接，以此实现动态SQL
     * 下面的函数中，如果有此指示的，作用均一样
     * 用法：
     * int age=-2;
     * builder.addWhereAnd("age<?",age,age>18);
     * 上面这样的语句将不会被拼接到其中
     * @param where 指定条件预处理，只能包含一个?占位符
     * @param val 占位符对应填入的值
     * @param testPass 是否满足填入拼接条件
     * @return
     */
    public SelectBuilder addWhereAnd(String where, Object val, boolean testPass){
        if(testPass){
            builder.append(" AND ");
            builder.append(where);
            params.add(val);
        }
        return this;
    }
    public SelectBuilder addWhereOr(String where, Object val, boolean testPass){
        if(testPass){
            builder.append(" OR ");
            builder.append(where);
            params.add(val);
        }
        return this;
    }

    /**
     * 上面两个方法的简化重载，把最常见的==null 和 "".equals两种情况排除
     * @param where
     * @param val
     * @return
     */
    public SelectBuilder addWhereAnd(String where, Object val){
        boolean pass=true;
        if(val==null){
            pass=false;
        }else{
            if(val instanceof String){
                if("".equals(val)){
                    pass=false;
                }
            }
        }
        return addWhereAnd(where,val,pass);
    }
    public SelectBuilder addWhereOr(String where, Object val){
        boolean pass=true;
        if(val==null){
            pass=false;
        }else{
            if(val instanceof String){
                if("".equals(val)){
                    pass=false;
                }
            }
        }
        return addWhereOr(where,val,pass);
    }

    /**
     * 添加一个in查询条件，如果vals字段为空或者大小为0，将不会被拼接
     * @param colName in作用的列名
     * @param vals in使用的值
     * @param spaceHolder 占位符，虽然MySQL中都是？
     * @return
     */
    public SelectBuilder addWhereAndIn(String colName, List<? extends Object> vals, String spaceHolder){
        if(vals!=null && vals.size()>0){
            builder.append(" AND ");
            builder.append(colName);
            builder.append(" IN (");
            StringBuilder inBuilder=new StringBuilder();
            for(int i=0;i<vals.size();i++){
                if(i!=0){
                    inBuilder.append(",");
                }
                inBuilder.append(spaceHolder);
                params.add(vals.get(i));
            }
            builder.append(inBuilder.toString());
            builder.append(")");
        }
        return this;
    }
    public SelectBuilder addWhereOrIn(String colName, List<? extends Object> vals, String spaceHolder){
        if(vals!=null && vals.size()>0){
            builder.append(" OR ");
            builder.append(colName);
            builder.append(" IN (");
            StringBuilder inBuilder=new StringBuilder();
            for(int i=0;i<vals.size();i++){
                if(i!=0){
                    inBuilder.append(",");
                }
                inBuilder.append(spaceHolder);
                params.add(vals.get(i));
            }
            builder.append(inBuilder.toString());
            builder.append(")");
        }

        return this;
    }
    public SelectBuilder addWhereAndIn(String colName, String spaceHolder, Object ... inParams){
        if(inParams.length>0){
            builder.append(" AND ");
            builder.append(colName);
            builder.append(" IN (");
            StringBuilder inBuilder=new StringBuilder();
            for(int i=0;i<inParams.length;i++){
                if(i!=0){
                    inBuilder.append(",");
                }
                inBuilder.append(spaceHolder);
                params.add(inParams[i]);
            }
            builder.append(inBuilder.toString());
            builder.append(")");
        }
        return this;
    }
    public<T> SelectBuilder addWhereOrIn(String colName, T[] inParams, String spaceHolder){
        if(inParams.length>0){
            builder.append(" OR ");
            builder.append(colName);
            builder.append(" IN (");
            StringBuilder inBuilder=new StringBuilder();
            for(int i=0;i<inParams.length;i++){
                if(i!=0){
                    inBuilder.append(",");
                }
                inBuilder.append(spaceHolder);
                params.add(inParams[i]);
            }
            builder.append(inBuilder.toString());
            builder.append(")");
        }

        return this;
    }
    public<T> SelectBuilder addWhereAndIn(String colName, T[] inParams, String spaceHolder){
        if(inParams.length>0){
            builder.append(" AND ");
            builder.append(colName);
            builder.append(" IN (");
            StringBuilder inBuilder=new StringBuilder();
            for(int i=0;i<inParams.length;i++){
                if(i!=0){
                    inBuilder.append(",");
                }
                inBuilder.append(spaceHolder);
                params.add(inParams[i]);
            }
            builder.append(inBuilder.toString());
            builder.append(")");
        }
        return this;
    }
    public SelectBuilder addWhereOrIn(String colName, String spaceHolder, Object ... inParams){
        if(inParams.length>0){
            builder.append(" OR ");
            builder.append(colName);
            builder.append(" IN (");
            StringBuilder inBuilder=new StringBuilder();
            for(int i=0;i<inParams.length;i++){
                if(i!=0){
                    inBuilder.append(",");
                }
                inBuilder.append(spaceHolder);
                params.add(inParams[i]);
            }
            builder.append(inBuilder.toString());
            builder.append(")");
        }

        return this;
    }

    /**
     * 添加Order by 的条件语句
     * 用法：
     * builder.addOrderBy("id desc");
     * @param order
     * @return
     */
    public SelectBuilder addOrderBy(String order){
        builder.append(" ORDER BY ");
        builder.append(order);
        return this;
    }

    /**
     * 添加Group by的语句
     * 用法：
     * builder.addGroupBy("id,name");
     * @param group
     * @return
     */
    public SelectBuilder addGroupBy(String group){
        builder.append(" GROUP BY ");
        builder.append(group);
        return this;
    }

    /**
     * 添加having子句
     * 用法：
     * builder.addHaving("age>? and weight<?",22,60);
     * @param having
     * @param vals
     * @return
     */
    public SelectBuilder addHaving(String having, Object ... vals){
        builder.append(" HAVING ");
        builder.append(having);
        for(Object item : vals){
            params.add(item);
        }
        return this;
    }

    /**
     * 添加分页查询子句
     * 用法：
     * builder.addLimit("?",0,15);
     * @param spaceHolder
     * @param indexOffset
     * @param pageSize
     * @return
     */
    public SelectBuilder addLimit(String spaceHolder, int indexOffset, int pageSize){
        builder.append(" LIMIT ");
        builder.append(spaceHolder);
        builder.append(",");
        builder.append(spaceHolder);
        params.add(indexOffset);
        params.add(pageSize);
        return this;
    }

    /**
     * 添加Inner Join 子句
     * 左右链接使用类似
     * 用法：
     * builder.addInnerJoin("Admin","Admin.RoleId=Role.id");
     * @param tableName
     * @param on
     * @return
     */
    public SelectBuilder addInnerJoin(String tableName, String on){
        builder.append(" INNER JOIN ");
        builder.append(tableName);
        builder.append(" ON ");
        builder.append(on);
        return this;
    }
    public SelectBuilder addLeftJoin(String tableName, String on){
        builder.append(" LEFT JOIN ");
        builder.append(tableName);
        builder.append(" ON ");
        builder.append(on);
        return this;
    }
    public SelectBuilder addRightJoin(String tableName, String on){
        builder.append(" RIGHT JOIN ");
        builder.append(tableName);
        builder.append(" ON ");
        builder.append(on);
        return this;
    }

}
