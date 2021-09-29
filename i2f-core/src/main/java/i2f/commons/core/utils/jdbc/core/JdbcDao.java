package i2f.commons.core.utils.jdbc.core;

import i2f.commons.core.data.Pair;
import i2f.commons.core.utils.jdbc.data.DBResultData;
import i2f.commons.core.utils.jdbc.data.PageContextData;
import i2f.commons.core.utils.jdbc.data.PageMeta;
import i2f.commons.core.utils.jdbc.wrapper.*;
import i2f.commons.core.utils.str.AppendUtil;
import lombok.Data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/9/27
 */
@Data
public class JdbcDao {
    private IJdbcMeta meta;
    private JdbcProvider provider;
    private TransactionManager transactionManager;


    public JdbcDao(IJdbcMeta meta) throws SQLException {
        this.meta = meta;
        this.transactionManager=new TransactionManager(this.meta);
        this.provider = new JdbcProvider(this.transactionManager);
    }

    public JdbcDao(TransactionManager transactionManager) {
        this.transactionManager=transactionManager;
        this.provider = new JdbcProvider(this.transactionManager);
        this.meta = this.transactionManager.getMeta();
    }

    public DBResultData queryNative(String sql) throws SQLException {
        return provider.query(sql);
    }

    public int insertNative(String sql) throws SQLException {
        return provider.update(sql);
    }

    public int updateNative(String sql) throws SQLException {
        return provider.update(sql);
    }

    public int deleteNative(String sql) throws SQLException {
        return provider.update(sql);
    }

    public int executeNative(String sql) throws SQLException {
        return provider.update(sql);
    }

    public DBResultData queryAll(String tableName) throws SQLException {
        String sql = AppendUtil.builder()
                .add("select * from ").add(tableName)
                .done();
        return provider.query(sql);
    }

    public int emptyTable(String tableName) throws SQLException {
        String sql = AppendUtil.builder()
                .add("delete from ").add(tableName)
                .done();
        return provider.update(sql);
    }

    public int dropTable(String tableName) throws SQLException {
        String sql = AppendUtil.builder()
                .add("drop table if exists ").add(tableName)
                .done();
        return provider.update(sql);
    }

    public long countTable(String tableName) throws SQLException {
        String sql = AppendUtil.builder()
                .add("select count(*) from ").add(tableName)
                .done();
        DBResultData rs = provider.query(sql);
        return (Long) rs.getData(0, 0);
    }

    public DBResultData queryCommon(QueryWrapper wrapper) throws SQLException {
        List<Object> params = new ArrayList<>();
        AppendUtil.AppendBuilder builder = AppendUtil.builder();
        SqlInflater.sqlQueryCommon(builder, wrapper, params);

        String sql = builder.done();
        return provider.queryRaw(sql, params);
    }

    public long queryCountCommon(QueryWrapper wrapper) throws SQLException {
        List<Object> params = new ArrayList<>();
        AppendUtil.AppendBuilder builder = AppendUtil.builder();
        builder.add(" select count(*) cnt ")
                .add(" from ( ");
        SqlInflater.sqlQueryCommon(builder, wrapper, params);
        builder.add(" ) tmp ");

        String sql = builder.done();
        DBResultData rs = provider.queryRaw(sql, params);
        return (Long) rs.getData(0, 0);
    }

    public int deleteCommon(DeleteWrapper wrapper) throws SQLException {
        List<Object> params = new ArrayList<>();
        AppendUtil.AppendBuilder builder = AppendUtil.builder();
        builder.add(" delete from ").add(wrapper.tableName);
        String where = SqlInflater.sqlBaseWhereConditionPart(wrapper, params);
        SqlInflater.trimSql(builder,where," where "," ","and","or");

        String sql = builder.done();
        int effecLine = provider.updateRaw(sql, params);
        return effecLine;
    }

    public int updateCommon(UpdateWrapper wrapper) throws SQLException {
        List<Object> params = new ArrayList<>();
        AppendUtil.AppendBuilder builder = AppendUtil.builder();
        builder.add(" update ").add(wrapper.tableName)
                .add(" set ");
        AppendUtil.AppendBuilder inBuilder = AppendUtil.builder();

        inBuilder.addCollection((val,appender,args)->{
            Pair<String, Object> item=(Pair<String, Object>)val;
            List<Object> plist=(List<Object>)args[0];
            AppendUtil.appender(appender)
                .add(" ").add(item.key).add(" = ").add(" ? ");
            plist.add(item.val);

        },new Object[]{params},true,",",null,null,wrapper.valPairs);


        if (wrapper.nativeValPairs != null && wrapper.nativeValPairs.size() > 0) {
            inBuilder.add(" , ");
            inBuilder.addCollection((val,appender,args)->{
                Pair<String, String> item=(Pair<String, String>)val;
                AppendUtil.appender(appender)
                        .add(" ").add(item.key).add(" ").add(item.val);
            },null,true,",",null,null,wrapper.nativeValPairs);

        }

        String part = inBuilder.done();
        SqlInflater.trimSql(builder,part," "," ",",");


        String where = SqlInflater.sqlBaseWhereConditionPart(wrapper, params);
        SqlInflater.trimSql(builder,where," where "," ","and","or");

        String sql = builder.done();
        int effecLine = provider.updateRaw(sql, params);
        return effecLine;
    }

    public int insertCommon(InsertWrapper wrapper) throws SQLException {
        List<Object> params = new ArrayList<>();
        AppendUtil.AppendBuilder builder = AppendUtil.builder()
                .add("insert into ").add(wrapper.tableName)
                .add(" ( ");
        AppendUtil.AppendBuilder inBuilder = AppendUtil.builder();

        inBuilder.addCollection((val,appender,args)->{
            Pair<String, Object> item=(Pair<String, Object>)val;
            AppendUtil.appender(appender).add(item.key);
        },null,true,",",null,null,wrapper.valPairs);

        if (wrapper.nativeValPairs != null && wrapper.nativeValPairs.size() > 0) {
            inBuilder.add(" , ");
            inBuilder.addCollection((val,appender,args)->{
                Pair<String, String> item=(Pair<String, String>)val;
                AppendUtil.appender(appender).add(item.key);
            },null,true,",",null,null,wrapper.nativeValPairs);

        }

        String part = inBuilder.done();
        SqlInflater.trimSql(builder,part," "," ",",");
        builder.add(" ) ")
                .add(" values ")
                .add(" ( ");
        inBuilder = AppendUtil.builder();

        inBuilder.addCollection((val,appender,args)->{
            Pair<String, Object> item=(Pair<String, Object>)val;
            List<Object> plist=(List<Object>)args[0];
            AppendUtil.appender(appender).add(" ? ");
            plist.add(item.val);
        },new Object[]{params},true,",",null,null,wrapper.valPairs);

        if (wrapper.nativeValPairs != null && wrapper.nativeValPairs.size() > 0) {
            inBuilder.add(" , ");
            inBuilder.addCollection((val,appender,args)->{
                Pair<String, String> item=(Pair<String, String>)val;
                AppendUtil.add(appender,item.val);
            },null,true,",",null,null,wrapper.nativeValPairs);

        }

        part = inBuilder.done();
        SqlInflater.trimSql(builder,part," "," ",",");
        builder.add(" ) ");

        String sql = builder.done();
        int effecLine = provider.updateRaw(sql, params);
        return effecLine;
    }

    public long insertCommonReturnId(InsertWrapper wrapper) throws SQLException {
        int effecLine = insertCommon(wrapper);
        String sql = "select @@IDENTITY";
        DBResultData rs = provider.query(sql);
        Long id = rs.getData(0, 0);
        return id;
    }

    public int insertCommonBatch(InsertBatchWrapper wrapper) throws SQLException {
        List<Object> params = new ArrayList<>(2048);
        AppendUtil.AppendBuilder builder = AppendUtil.builder();
        builder.add("insert into ").add(wrapper.tableName)
                .add(" ( ");

        boolean isFirst = true;
        for (Map.Entry<String, Object> item : wrapper.cols.entrySet()) {
            if (!isFirst) {
                builder.add(",");
            }
            builder.add(" ").add(item.getKey());
            isFirst = false;
        }
        builder.add(" ) ")
                .add(" values ");
        builder.addCollection((val,appender,args)->{
            Map<String, Object> item=(Map<String, Object>)val;
            List<Object> plist=(List<Object>)args[0];
            AppendUtil.AppendBuilder builder1=AppendUtil.appender(appender)
                    .add(" ( ");
            boolean isChildFirst = true;
            for (Map.Entry<String, Object> pit : item.entrySet()) {
                if (!isChildFirst) {
                    builder1.add(",");
                }
                builder1.add(" ? ");
                plist.add(pit.getValue());
                isChildFirst = false;
            }
            builder1.add(" ) ");
        },new Object[]{params},true,",",null,null,wrapper.multiVals);


        String sql = builder.done();
        int effecLine = provider.updateRaw(sql, params);
        return effecLine;
    }

    public PageContextData queryPage(QueryWrapper wrapper) throws SQLException {
        List<Object> params = new ArrayList<>();
        AppendUtil.AppendBuilder builder = AppendUtil.builder();
        SqlInflater.sqlQueryCommon(builder, wrapper, params);

        String sql = builder.done();
        Long idx = null, lmt = null;
        if (wrapper.pageIndex != null) {
            int iidx = wrapper.pageIndex;
            idx = (long) iidx;
        }
        if (wrapper.pageLimit != null) {
            int ilmt = wrapper.pageLimit;
            lmt = (long) ilmt;
        }
        PageMeta page = new PageMeta(idx, lmt);
        PageContextData ctx = provider.pageRaw(page, sql, params);
        return ctx;
    }
}
