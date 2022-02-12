package i2f.commons.component.mybatis.plugin.sql;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

/**
 * @author ltb
 * @date 2021/12/23 21:25
 * @desc
 */
public class SqlSourceProvider {
    /**
     * 给定Mybatis的配置conf和你的动态sql以及这个SQL对应的参数类型
     * 返回一个可用的SQLSource，可用于在拦截器Interceptor中实现动态的SQL整个替换
     * @param conf
     * @param sql
     * @param parameterType
     * @return
     */
    public static SqlSource getByXmlScript(Configuration conf, String sql, Class<?> parameterType){
        XMLLanguageDriver driver=new XMLLanguageDriver();
        String script = sql.trim();
        if(!script.startsWith("<script>")){
            script="<script>"+script+"</script>";
        }
        return driver.createSqlSource(conf,script, parameterType);
    }
}
