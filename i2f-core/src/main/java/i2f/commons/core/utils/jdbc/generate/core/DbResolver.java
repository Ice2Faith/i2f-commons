package i2f.commons.core.utils.jdbc.generate.core;


import i2f.commons.core.utils.jdbc.generate.data.TableColumnMeta;
import i2f.commons.core.utils.jdbc.generate.data.TableMeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/8/25
 */
public class DbResolver {
    public static TableMeta getTableMeta(Connection conn, String tableName) throws SQLException {
        String sql="select * from "+tableName+" where 1!=1";
        Statement stat=conn.createStatement();
        ResultSet rs=stat.executeQuery(sql);
        ResultSetMetaData meta=rs.getMetaData();
        TableMeta ret= inflateTableMeta(meta);
        rs.close();
        stat.close();
        return ret;
    }

    public static TableMeta inflateTableMeta(ResultSetMetaData meta) throws SQLException {
        TableMeta ret=new TableMeta();
        String schema=meta.getSchemaName(1);
        String table=meta.getTableName(1);
        ret.setSchema(schema);
        ret.setTableName(table);

        List<TableColumnMeta> cols=inflateTableColumnMeta(meta);
        ret.setColumns(cols);

        return ret;
    }

    public static List<TableColumnMeta> inflateTableColumnMeta(ResultSetMetaData meta) throws SQLException {
        List<TableColumnMeta> ret=new ArrayList<>();
        int colCount=meta.getColumnCount();
        for(int i=1;i<=colCount;i++){
            TableColumnMeta col=new TableColumnMeta();
            String colName=meta.getColumnName(i);
            int colType=meta.getColumnType(i);
            String colTypeName=meta.getColumnTypeName(i);
            String colLabel=meta.getColumnLabel(i);
            String colClassName=meta.getColumnClassName(i);
            int colDisplaySize=meta.getColumnDisplaySize(i);

            col.setColName(colName);
            col.setColType(colType);
            col.setColTypeName(colTypeName);
            col.setColDisplaySize(colDisplaySize);
            col.setJavaTypeString(colClassName);
            try{
                Class clazz=Class.forName(colClassName);
                col.setJavaType(clazz);
            }catch (Throwable t){

            }
            ret.add(col);
        }
        return ret;
    }
}
