package test.wrapper;

import i2f.commons.core.data.BinTreeNode;
import i2f.commons.core.data.Pair;
import i2f.commons.core.data.Triple;
import i2f.commons.core.utils.db.DBClassUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author ltb
 * @date 2021/9/28
 */
public class TestMap {
    public static void main(String[] args) throws IOException, SQLException {
//        File file=new File("D:\\IDEA_ROOT\\i2f-commons\\i2f-component\\pom.xml");
//        List<String> lines= FileUtil.readTxtFile(file,0,-1,true,true,(val)->{
//            return val.startsWith("<!");
//        },(val)->{
//            return val.replace("<!","#");
//        });
//
//        for(String item : lines){
//            System.out.println(item);
//        }


//        IJdbcMeta meta=TestWrapperJdbc.getJdbcMeta();
//        JdbcDao dao=new JdbcDao(meta);
//        TableMeta tm= DbResolver.getTableMeta(dao.getTransactionManager().getConnection(),"NoteT");
//
//        GenerateContext ctx= GenerateContext.build()
//                .basePackage("com.i2f")
//                .author("Ugex.Savelar")
//                .table(tm).types(DbGenType.ALL)
//                .save2File(true)
//                .savePath("D:\\IDEA_ROOT\\i2f-commons\\i2f-component\\src\\test\\java")
//                .done();
//
//        ctx= DbGenerator.genCodeFiles(ctx);


        String sql= DBClassUtil.genCreateTableByBean(UserBean.class);
        System.out.println(sql);

        sql=DBClassUtil.genDropTableByBean(UserBean.class,true);
        System.out.println(sql);

        System.out.println("-----------------------");
        sql=DBClassUtil.genTablesByBean(true,UserBean.class, BinTreeNode.class, Pair.class, Triple.class);
        System.out.println(sql);
    }
}
