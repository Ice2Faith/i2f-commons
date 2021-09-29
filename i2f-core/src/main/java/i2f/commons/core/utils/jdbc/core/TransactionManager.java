package i2f.commons.core.utils.jdbc.core;


import java.sql.Connection;
import java.sql.SQLException;


public class TransactionManager {
    public enum Isolation{
        READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
        READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
        REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
        SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),
        NONE(Connection.TRANSACTION_NONE);

        private int level;
        private Isolation(int level){
            this.level=level;
        }
        public int getLevel(){
            return level;
        }
    }

    private boolean openTrans=false;

    private IJdbcMeta meta;
    private Connection conn;

    public boolean isOpenTrans(){
        return openTrans;
    }

    public TransactionManager openTrans(boolean openTrans){
        this.openTrans=openTrans;
        return this;
    }

    public TransactionManager setMeta(IJdbcMeta meta) throws SQLException {
        this.meta=meta;
        this.conn=getConnection();
        return this;
    }
    public IJdbcMeta getMeta(){
        return meta;
    }
    public TransactionManager(IJdbcMeta meta) throws SQLException {
        this.meta=meta;
        this.conn=getConnection();
    }
    public TransactionManager(Connection conn){
        this.conn=conn;
    }

    public Connection getConnection() throws SQLException {
        if(this.conn!=null){
            if(this.conn.isClosed()){
                this.conn=JdbcProvider.getConnection(meta);
            }
        }else{
            this.conn=JdbcProvider.getConnection(meta);
        }

        return this.conn;
    }
    public TransactionManager begin() throws SQLException {
        begin(getConnection());
        return this;
    }
    public TransactionManager commit() throws SQLException {
        commit(getConnection());
        return this;
    }
    public TransactionManager rollback() throws SQLException {
        rollback(getConnection());
        return this;
    }
    public TransactionManager close() throws SQLException {
        getConnection().close();
        return this;
    }

    public TransactionManager isolation(Isolation iso) throws SQLException {
        isolation(getConnection(),iso);
        return this;
    }

    public static void isolation(Connection conn,Isolation iso) throws SQLException {
        conn.setTransactionIsolation(iso.getLevel());
    }
    public static void begin(Connection conn) throws SQLException {
        try {
            conn.setAutoCommit(false);
        } catch (Exception e) {
            throw new SQLException("begin transaction error:setAutoCommit(false)",e);
        }
    }
    public static void commit(Connection conn) throws SQLException {
        try {
            conn.commit();
        } catch (Exception e) {
            throw new SQLException("commit transaction error",e);
        }
    }
    public static void rollback(Connection conn) throws SQLException {
        try {
            conn.rollback();
        } catch (Exception e) {
            throw new SQLException("rollback transaction error",e);
        }
    }
}
