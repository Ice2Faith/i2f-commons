package i2f.commons.component.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author ltb
 * @date 2021/10/9
 */
@Intercepts(@Signature(type=StatementHandler.class,method = "*",args = {}))
public class AutoPageMybatisInterceptor  implements Interceptor {
    protected Properties properties = new Properties();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target=invocation.getTarget();
        Object[] arg=invocation.getArgs();
        Method method=invocation.getMethod();

        if(target instanceof Executor){

        }else if(target instanceof ParameterHandler){

        }else if(target instanceof ResultSetHandler){

        }else if(target instanceof StatementHandler){
            StatementHandler handler=(StatementHandler)target;
            BoundSql boundSql= handler.getBoundSql();
            String sql=boundSql.getSql();


        }
        Object rs=invocation.proceed();
        return rs;
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
