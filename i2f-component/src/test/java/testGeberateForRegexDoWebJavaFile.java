import i2f.commons.core.utils.file.FileUtil;
import i2f.commons.core.utils.jdbc.core.IJdbcMeta;
import i2f.commons.core.utils.jdbc.core.JdbcDao;
import i2f.commons.core.utils.jdbc.generate.DbTplGenerator;
import i2f.commons.core.utils.jdbc.generate.core.DbResolver;
import i2f.commons.core.utils.jdbc.generate.data.DbGenType;
import i2f.commons.core.utils.jdbc.generate.data.GenerateContext;
import i2f.commons.core.utils.jdbc.generate.data.TableMeta;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ltb
 * @date 2021/10/26
 */
public class testGeberateForRegexDoWebJavaFile {
    public static void main(String[] args) throws IOException, SQLException {
        File tplFile=getResourceFile("tpl/AxiosRequest.tpl");
        String tpl= FileUtil.loadTxtFile(tplFile);

        JdbcDao dao=getDao();
        Connection conn=dao.getTransactionManager().getConnection();
        TableMeta meta= DbResolver.getTableMeta(conn,"student_course");

        GenerateContext ctx=new GenerateContext();
        ctx.meta=meta;
        ctx.basePackage="com.i2f";
        ctx.author="ltb";

        ctx.save2File=true;
        ctx.genType= DbGenType.ALL.getCode();
        ctx.savePath="D:\\IDEA_ROOT\\i2f-commons\\i2f-component\\src\\test\\java";

        DbTplGenerator.genCodeFiles(ctx);
    }

    public static File getResourceFile(String fileName){
        URL url=Thread.currentThread().getContextClassLoader().getResource(fileName);
        return new File(url.getFile());
    }

    public static JdbcDao getDao() throws SQLException {
        JdbcDao dao=new JdbcDao(getJdbcMeta());
        return dao;
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
