package i2f.commons.core.utils.jdbc.core;

import i2f.commons.core.utils.data.DateUtil;
import i2f.commons.core.utils.jdbc.data.DBResultData;
import i2f.commons.core.utils.jdbc.data.PageContextData;
import i2f.commons.core.utils.jdbc.data.PageMeta;
import i2f.commons.core.utils.str.AppendUtil;
import lombok.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/9/27
 */
@Data
public class JdbcProvider {
    public static  boolean showLog=true;

    private TransactionManager transactionManager;

    public static void logout(Date date,Statement stat,String sql,List<Object> params){
        if(showLog){
            String log=AppendUtil.builder()
                    .addsLine("JdbcLogOut -> time:", DateUtil.format(date))
                    .addsLine("\tstat: ",stat)
                    .addsLine("\tsql : ",sql)
                    .adds("\tparams : ")
                    .addCollection(false,",","[","]",params)
                    .done();
            System.out.println(log);
        }
    }
    public static void logout(Date date,Object ... objs){
        if(showLog){
            String log=AppendUtil.buffer()
                    .adds("JdbcLogOut -> time:",DateUtil.format(date)," : ")
                    .adds(objs)
                    .done();
            System.out.println(log);
        }
    }


    public JdbcProvider(TransactionManager transactionManager){
        this.transactionManager=transactionManager;
    }

    public static void registryDriverClass(String className) throws SQLException {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Db driver class not found, className="+className,e);
        }
    }
    public Connection getConnection() throws SQLException {
        return transactionManager.getConnection();
    }

    public static Connection getConnection(IJdbcMeta meta) throws SQLException {
        registryDriverClass(meta.getDriverClassName());
        Connection conn= DriverManager.getConnection(meta.getUrl(), meta.getUsername(), meta.getPassword());
        return conn;
    }

    public static void closeRes(Connection conn, Statement stat, ResultSet rs) throws SQLException {
        if(rs!=null){
            rs.close();
        }
        if(stat!=null){
            stat.close();
        }
        if(conn!=null){
            conn.close();
        }
    }

    public boolean execute(String prepareSql, Object ... params) throws SQLException {
        boolean ret= execute(getConnection(),prepareSql,params);
        if(!transactionManager.isOpenTrans()){
            transactionManager.close();
        }
        return ret;
    }
    public boolean executeRaw(String prepareSql, List<Object> params) throws SQLException {
        boolean ret= executeRaw(getConnection(),prepareSql,params);
        if(!transactionManager.isOpenTrans()){
            transactionManager.close();
        }
        return ret;
    }
    public static boolean execute(Connection conn, String prepareSql, Object ... params) throws SQLException {
        List<Object> args=new ArrayList<>(params.length);
        for(Object item : params){
            args.add(item);
        }
        return executeRaw(conn,prepareSql,args);
    }
    public static boolean executeRaw(Connection conn, String prepareSql, List<Object> params) throws SQLException {
        PreparedStatement stat=PreparedStatementBuilder.makeByList(conn,prepareSql,params);
        Date date=new Date(System.currentTimeMillis());
        logout(date,stat,prepareSql,params);
        boolean rs= stat.execute();
        logout(date,"execute result : ",rs);
        closeRes(null,stat,null);
        return rs;
    }

    public int update(String prepareSql, Object ... params) throws SQLException {
        int ret= update(getConnection(),prepareSql,params);
        if(!transactionManager.isOpenTrans()){
            transactionManager.close();
        }
        return ret;
    }
    public int updateRaw(String prepareSql, List<Object> params) throws SQLException {
        int ret= updateRaw(getConnection(),prepareSql,params);
        if(!transactionManager.isOpenTrans()){
            transactionManager.close();
        }
        return ret;
    }
    public static int update(Connection conn, String prepareSql, Object ... params) throws SQLException {
        List<Object> args=new ArrayList<>(params.length);
        for(Object item : params){
            args.add(item);
        }
        return updateRaw(conn,prepareSql,args);
    }
    public static int updateRaw(Connection conn, String prepareSql, List<Object> params) throws SQLException {
        PreparedStatement stat=PreparedStatementBuilder.makeByList(conn,prepareSql,params);
        Date date=new Date(System.currentTimeMillis());
        logout(date,stat,prepareSql,params);
        int rs= stat.executeUpdate();
        logout(date,"update result : ",rs);
        closeRes(null,stat,null);
        return rs;
    }

    public DBResultData query(String prepareSql, Object ... params) throws SQLException {
        DBResultData ret= query(getConnection(),prepareSql,params);
        if(!transactionManager.isOpenTrans()){
            transactionManager.close();
        }
        return ret;
    }
    public DBResultData queryRaw(String prepareSql, List<Object> params) throws SQLException {
        DBResultData ret= queryRaw(getConnection(),prepareSql,params);
        if(!transactionManager.isOpenTrans()){
            transactionManager.close();
        }
        return ret;
    }
    public static DBResultData query(Connection conn, String prepareSql, Object ... params) throws SQLException {
        List<Object> args=new ArrayList<>(params.length);
        for(Object item : params){
            args.add(item);
        }
        return queryRaw(conn,prepareSql,args);
    }
    public static DBResultData queryRaw(Connection conn, String prepareSql, List<Object> params) throws SQLException {
        PreparedStatement stat=PreparedStatementBuilder.makeByList(conn,prepareSql,params);
        Date date=new Date(System.currentTimeMillis());
        logout(date,stat,prepareSql,params);
        ResultSet rs= stat.executeQuery();
        DBResultData ret=parseResultSet(rs);
        logout(date,"query result : ",ret.getCountRows());
        closeRes(null,stat,rs);
        return ret;
    }


    public PageContextData page(PageMeta page,String prepareSql,Object ... params) throws SQLException {
        PageContextData ret= page(page,getConnection(),prepareSql,params);
        if(!transactionManager.isOpenTrans()){
            transactionManager.close();
        }
        return ret;
    }
    public static PageContextData page(PageMeta page,Connection conn,String prepareSql,Object ... params) throws SQLException {
        List<Object> args=new ArrayList<>(params.length);
        for(Object item : params){
            args.add(item);
        }
        return pageRaw(page,conn,prepareSql,args);
    }
    public PageContextData pageRaw(PageMeta page,String prepareSql,List<Object> params) throws SQLException {
        PageContextData ret= pageRaw(page,getConnection(),prepareSql,params);
        if(!transactionManager.isOpenTrans()){
            transactionManager.close();
        }
        return ret;
    }
    public static PageContextData pageRaw(PageMeta page,Connection conn,String prepareSql,List<Object> params) throws SQLException {
        return PageProvider.queryPage(page,conn,prepareSql,params);
    }
    public static DBResultData parseResultSet(ResultSet rs) throws SQLException {
        List<String> cols=new ArrayList<>(32);
        List<Map<String,Object>> datas=new ArrayList<>(256);
        try{
            ResultSetMetaData meta=rs.getMetaData();
            int colCount=meta.getColumnCount();
            for(int i=1;i<=colCount;i++){
                String colName=meta.getColumnName(i);
                cols.add(colName);
            }
            while(rs.next()){
                Map<String,Object> row=new HashMap<>();
                for(int i=1;i<=colCount;i++){
                    String colName=meta.getColumnName(i);
                    Object colValue=rs.getObject(colName);
                    row.put(colName,colValue);
                }
                datas.add(row);
            }

        }catch(Exception e){
            throw new SQLException("read DataSet error",e);
        }

        return new DBResultData(cols,datas);
    }
}
