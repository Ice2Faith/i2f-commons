package test.wrapper;

import i2f.commons.core.utils.data.MapUtil;
import i2f.commons.core.utils.jdbc.core.IJdbcMeta;
import i2f.commons.core.utils.jdbc.core.JdbcDao;
import i2f.commons.core.utils.jdbc.data.DBResultData;
import i2f.commons.core.utils.jdbc.data.PageContextData;
import i2f.commons.core.utils.jdbc.wrapper.*;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlBase;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author ltb
 * @date 2021/9/27
 */
public class TestWrapperJdbc {
    public static void main(String[] args) throws SQLException {
        IJdbcMeta meta=getJdbcMeta();
        JdbcDao dao=new JdbcDao(meta);
        String tableName="notet";

        long count=dao.countTable(tableName);

        DBResultData rs=dao.queryAll(tableName);

        QueryWrapper wrapper=QueryWrapper.build()
                .table(tableName)
                .cols("id","title","viceTitle","content","createDate","viewCount")
                .gte(SqlBase.LinkType.AND,"id",105)
                .lt(SqlBase.LinkType.AND,"id",150)
                .like(SqlBase.LinkType.AND,"title","aa")
                .like(SqlBase.LinkType.AND,"viceTitle","tes","est")
                .in(SqlBase.LinkType.AND,"viewCount",1,2,3,4,5,6,7,8)
                .asc("id")
                .desc("viewCount")
                .page(0,10)
                .done();

        PageContextData data=dao.queryPage(wrapper);

        dao.dropTable("user");

        String sql="create table User ( id int(11) auto_increment primary key, name varchar(50) ,age int(11) ,intro varchar(300),sex char(1) )";
        dao.executeNative(sql);

        InsertWrapper insertWrapper=InsertWrapper.build()
                .table("User")
                .add("name","ugex")
                .add("age",22)
                .add("intro","I'm the god")
                .add("sex",1)
                .done();

        dao.countTable("user");

        dao.insertCommon(insertWrapper);

        wrapper=QueryWrapper.build()
                .table("User")
                .col("*")
                .eq(SqlBase.LinkType.AND,"id",1)
                .done();
        rs=dao.queryCommon(wrapper);

        int id=rs.getData(0,"id");

        UpdateWrapper updateWrapper=UpdateWrapper.build()
                .table("User")
                .add("name","Ugex.Savelar")
                .add("intro","I'm the god of jdbc dao.")
                .eq(SqlBase.LinkType.AND,"id",id)
                .done();

        dao.updateCommon(updateWrapper);

        DeleteWrapper deleteWrapper=DeleteWrapper.build()
                .table("User")
                .eq(SqlBase.LinkType.AND,"id",id)
                .done();

        InsertBatchWrapper insertBatchWrapper=InsertBatchWrapper.build()
                .table("User")
                .cols("name","age")
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",12))
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",13))
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",14))
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",15))
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",16))
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",17))
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",18))
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",19))
                .add(MapUtil.adds(new HashMap<String,Object>(),new String[]{"name","age"},"Ugex",20))
                .done();

        dao.insertCommonBatch(insertBatchWrapper);

        dao.queryAll("user");

        dao.deleteCommon(deleteWrapper);

        dao.emptyTable("user");

        dao.dropTable("user");

    }

    public static IJdbcMeta getJdbcMeta(){
        IJdbcMeta meta=new IJdbcMeta() {
            @Override
            public String getDriverClassName() {
                return "com.mysql.cj.jdbc.Driver";
            }

            @Override
            public String getUrl() {
                return "jdbc:mysql://localhost:3306/SingleNoteDB?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8";
            }

            @Override
            public String getUsername() {
                return "root";
            }

            @Override
            public String getPassword() {
                return "ltb12315";
            }
        };
        return meta;
    }
}
