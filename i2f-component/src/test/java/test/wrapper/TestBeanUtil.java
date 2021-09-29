package test.wrapper;

import i2f.commons.core.utils.jdbc.core.IJdbcMeta;
import i2f.commons.core.utils.jdbc.core.JdbcDao;
import i2f.commons.core.utils.jdbc.JdbcDaoUtil;
import i2f.commons.core.utils.jdbc.core.TransactionManager;
import i2f.commons.core.utils.jdbc.data.DBPageData;
import i2f.commons.core.utils.jdbc.wrapper.QueryWrapper;

import java.sql.SQLException;

/**
 * @author ltb
 * @date 2021/9/28
 */
public class TestBeanUtil {
    public static void main(String[] args) throws SQLException {
        UserBean bean=new UserBean();
        bean.setUserId(1);
        bean.setUserName("account");
        bean.setUserIntro("userName");
        bean.setUserAge(22);
        bean.setUserSex(1);

        IJdbcMeta meta=TestWrapperJdbc.getJdbcMeta();
        JdbcDao dao=new JdbcDao(meta);
        TransactionManager transactionManager=dao.getTransactionManager();

        JdbcDaoUtil daoUtil=new JdbcDaoUtil(dao);

        //testJdbcJpa(bean, dao, daoUtil);

        daoUtil.insertAutoPk(bean);

        transactionManager.openTrans(true)
                .isolation(TransactionManager.Isolation.READ_COMMITTED)
                .begin();

        daoUtil.insertAutoPk(bean);

        transactionManager.commit();

        dao.emptyTable(daoUtil.getTableName(UserBean.class));

        transactionManager.rollback();

        transactionManager.close();

    }

    private static void testJdbcJpa(UserBean bean, JdbcDao dao, JdbcDaoUtil daoUtil) throws SQLException {
        dao.dropTable(daoUtil.getTableName(UserBean.class));

        String sql="create table user(" +
                "user_id int(11) auto_increment primary key," +
                "user_name varchar(300)," +
                "user_intro varchar(300)," +
                "user_age int(11)," +
                "user_sex char(1)" +
                ")";
        dao.executeNative(sql);

        daoUtil.insert(bean);

        daoUtil.insertAutoPk(bean);

        bean = daoUtil.queryByPk(UserBean.class,"user_id",1,"*");

        bean = daoUtil.queryByPkAnn(UserBean.class,2,"*");

        bean.setUserIntro("your father");
        daoUtil.updateByPk(bean);

        QueryWrapper wrapper=QueryWrapper.build()
                .page(0,10)
                .cols("*")
                .done();
        DBPageData<UserBean> page= daoUtil.page(UserBean.class,wrapper);
        System.out.println("page:"+page);

        daoUtil.deleteByPk(UserBean.class, bean.getUserId());

        dao.emptyTable(daoUtil.getTableName(UserBean.class));

        dao.queryAll(daoUtil.getTableName(UserBean.class));


        dao.queryAll(daoUtil.getTableName(UserBean.class));

        dao.dropTable(daoUtil.getTableName(UserBean.class));
    }
}
