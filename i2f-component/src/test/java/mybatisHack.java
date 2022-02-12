import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

/**
 * @author ltb
 * @date 2021/12/23 21:04
 * @desc
 */
public class mybatisHack {
    //mybatis 解析xml动态sql
    //https://www.cnblogs.com/siye1989/p/11622222.html
    public static void main(String[] args){
        String sqlScript="select * from t_test " +
                "<where> " +
                "   <if test=\"id!=null\">" +
                "   id=#{id}" +
                "   </if>" +
                "</where>";


    }


}
