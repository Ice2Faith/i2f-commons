package i2f.commons.component.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MyBatisUtil {
    public static<T,E> E Do(Class<T> daoClazz,IMyBatisDao<T,E> ido,Object ... params){
        SqlSession sqlSession=getSqlSession();
        T dao=getDao(sqlSession,daoClazz);
        E ret=ido.toDo(dao,params);
        commitAndClose(sqlSession);
        return ret;
    }

    private static volatile SqlSessionFactory sqlSessionFactory;
    private static volatile String CONFIG_PATH;
    //arg such as : "resources/mybatis.xml"
    public static void Initial(String configPath){
        CONFIG_PATH=configPath;
    }
    public static SqlSession getSqlSession(){
        if(CONFIG_PATH==null){
            throw new RuntimeException("MyBatisUtil's Initial() method must be invoked before use it.");
        }
        try{
            if(sqlSessionFactory==null){
                synchronized (MyBatisUtil.class){
                    InputStream is= Resources.getResourceAsStream(CONFIG_PATH);
                    if(sqlSessionFactory==null){
                        sqlSessionFactory=new SqlSessionFactoryBuilder().build(is);
                    }
                }
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return sqlSessionFactory.openSession();
    }
    public static void commitAndClose(SqlSession sqlSession){
        sqlSession.commit();
        sqlSession.close();
    }
    public static <T> T getDao(SqlSession sqlSession,Class<T> clazz){
        return sqlSession.getMapper(clazz);
    }
}
