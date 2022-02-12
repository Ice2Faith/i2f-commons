package i2f.commons.component.mybatis.plugin.interceptor;

import i2f.commons.component.mybatis.plugin.annotation.SqlSourceInject;
import i2f.commons.component.mybatis.plugin.sql.SqlSourceProvider;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author ltb
 * @date 2021/10/9
 * @ref https://www.cnblogs.com/sanzao/p/11423849.html
 *  https://blog.csdn.net/weixin_34281477/article/details/92080163
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class DynamicSqlSourceInterceptor implements Interceptor {
    protected Properties properties = new Properties();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();

        if(checkNeedReplaceSqlSource(invocation)){
            replaceSqlSource(ms,parameter,invocation);
        }

        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }


        Object rs=invocation.proceed();
        return rs;
    }

    private boolean checkNeedReplaceSqlSource(Invocation invocation){
        Method method= invocation.getMethod();
        SqlSourceInject ann=method.getAnnotation(SqlSourceInject.class);
        if(ann!=null){
            return ann.value();
        }
        return false;
    }

    private void replaceSqlSource(MappedStatement ms,Object parameter,Invocation invocation) throws NoSuchFieldException, IllegalAccessException {
        Class parameterClazz=Object.class;
        if(parameter!=null){
            parameterClazz=parameter.getClass();
        }

        String sql=resolveSql(ms,parameter,invocation);

        Class clazz= ms.getClass();
        Field field=clazz.getDeclaredField("sqlSource");
        field.setAccessible(true);

        Configuration conf= ms.getConfiguration();
        SqlSource sqlSource=SqlSourceProvider.getByXmlScript(conf,sql,parameterClazz);
        field.set(ms,sqlSource);
    }

    protected String resolveSql(MappedStatement ms,Object parameter,Invocation invocation){
        return "";
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties=properties;
    }


}
