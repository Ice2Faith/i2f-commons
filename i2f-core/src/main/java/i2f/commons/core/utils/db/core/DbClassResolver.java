package i2f.commons.core.utils.db.core;

import i2f.commons.core.utils.db.annotations.DBColumn;
import i2f.commons.core.utils.db.annotations.DBTable;
import i2f.commons.core.utils.reflect.core.resolver.base.AnnotationResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.FieldResolver;
import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.str.StringUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author ltb
 * @date 2021/9/29
 */
public class DbClassResolver {
    /**
     * 数据库常用关键字常量定义
     */

    public static final String NOT_NULL="NOT NULL";
    public static final String UNIQUE="UNIQUE";
    public static final String AUTO_INCREMENT="AUTO_INCREMENT";
    public static final String DEFAULT="DEFAULT";
    public static final String COMMENT="COMMENT";
    public static final String CHECK="CHECK";

    public static final String PRIMARY_KEY="PRIMARY KEY";

    public static final String FOREIGN_KEY="FOREIGN KEY";
    public static final String REFERENCES="REFERENCES";

    public static final String GROUP_BY="GROUP BY";
    public static final String HAVING="HAVING";

    public static final String ORDER_BY="ORDER BY";
    public static final String DESC="DESC";
    public static final String ASC="ASC";

    public static final String INNER_JOIN="INNER JOIN";
    public static final String ON="ON";
    public static final String LEFT_JOIN="LEFT JOIN";
    public static final String RIGHT_JOIN="RIGHT JOIN";

    /**
     * 数据库预处理占位符
     */
    public static final String PREPARE_SYMBOL="?";

    /**
     * Java常用类型与数据库类型对照表
     */
    private static Map<Class,String> typeMaps =new HashMap<>();
    public static String DEFAULT_STRING_TYPE="VARCHAR(300)";
    public static String DEFAULT_DOUBLE_TYPE="DOUBLE";
    public static String DEFAULT_DECIMAL="DECIMAL(16,4)";
    public static String DEFAULT_BOOLEAN="CHAR(1)";
    public static String DEFAULT_TIME="DATETIME";
    public static String DEFAULT_INT="INT";
    public static String DEFAULT_BIGINT="BIGINT";
    public static String DEFAULT_CHAR="VARCHAR(4)";
    public static String DEFAULT_BYTE="CHAR(1)";


    private static void initTypeMaps(){
        typeMaps.put(int.class,DEFAULT_INT);
        typeMaps.put(Integer.class,DEFAULT_INT);
        typeMaps.put(short.class,DEFAULT_INT);
        typeMaps.put(Short.class,DEFAULT_INT);

        typeMaps.put(byte.class,DEFAULT_BYTE);
        typeMaps.put(Byte.class,DEFAULT_BYTE);

        typeMaps.put(char.class,DEFAULT_CHAR);
        typeMaps.put(Character.class,DEFAULT_CHAR);

        typeMaps.put(BigInteger.class,DEFAULT_BIGINT);
        typeMaps.put(long.class,DEFAULT_BIGINT);
        typeMaps.put(Long.class,DEFAULT_BIGINT);

        typeMaps.put(String.class,DEFAULT_STRING_TYPE);

        typeMaps.put(double.class,DEFAULT_DOUBLE_TYPE);
        typeMaps.put(Double.class,DEFAULT_DOUBLE_TYPE);
        typeMaps.put(float.class,DEFAULT_DOUBLE_TYPE);
        typeMaps.put(Float.class,DEFAULT_DOUBLE_TYPE);
        typeMaps.put(BigDecimal.class,DEFAULT_DECIMAL);

        typeMaps.put(boolean.class,DEFAULT_BOOLEAN);
        typeMaps.put(Boolean.class,DEFAULT_BOOLEAN);

        typeMaps.put(java.util.Date.class,DEFAULT_TIME);
        typeMaps.put(java.sql.Date.class,DEFAULT_TIME);
        typeMaps.put(java.sql.Timestamp.class,DEFAULT_TIME);

        typeMaps.put(byte[].class,"BLOB");
    }
    private static String getDbType(Class type){
        if(typeMaps.size()==0){
            initTypeMaps();
        }
        if(typeMaps.containsKey(type)){
            return typeMaps.get(type);
        }
        return DEFAULT_STRING_TYPE;
    }

    public static String getTableName(Class clazz,DBTable tbann){
        String ret=clazz.getSimpleName();
        if(tbann!=null){
            if(!"".equals(tbann.table())){
                ret=tbann.table();
            }
            if(tbann.underScore()){
                ret= StringUtil.toUnderScore(ret);
            }
        }
        return ret;
    }

    public static String getSchemaName(Class clazz, DBTable tbann){
        String ret="";
        if(tbann!=null){
            if(!"".equals(tbann.schema())){
                ret=tbann.schema();
            }
        }
        return ret;
    }

    public static String getColumnName(Field field, DBColumn clnann){
        String name= field.getName();
        if(clnann!=null){
            if(clnann.underScore()){
                name= StringUtil.toUnderScore(name);
            }
            if(!"".equals(clnann.name())){
                name= clnann.name();
            }
        }
        return name;
    }

    public static String getColumnType(Field field,DBColumn clnann){
        Class fieldType=field.getType();
        String type=getDbType(fieldType);
        if(clnann!=null){
            if(clnann.text()){
                type="TEXT";
            }
            if(!"".equals(clnann.type())){
                type= clnann.type();
            }
        }
        return type;
    }
    public static String getDefaultValueString(Class type,String defVal){
        if(defVal==null || "".equals(defVal)){
            return defVal;
        }
        if(defVal.startsWith("'")){
            return defVal;
        }
        try{
            BigDecimal val=new BigDecimal(defVal);
            return defVal;
        }catch(Exception e){
            return "'"+defVal+"'";
        }
    }
    public static String getColumnRestrict(Field field,DBColumn clnann){
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        if(clnann!=null){
            if(!"".equals(clnann.restrict())){
                builder.adds(clnann.restrict());
            }
            else {
                if(clnann.primaryKey()){
                    builder.adds(" ",PRIMARY_KEY);
                }
                if(clnann.autoIncrement()){
                    builder.adds(" ",AUTO_INCREMENT);
                }
                if(!"".equals(clnann.foreignKey())){
                    builder.adds(" ",FOREIGN_KEY," ",REFERENCES,"(",clnann.foreignKey(),")");
                }
                if(clnann.unique()){
                    builder.adds(" ",UNIQUE);
                }
                if(clnann.notNull()){
                    builder.adds(" ",NOT_NULL);
                }
                if(!"".equals(clnann.defaultRt())){
                    builder.adds(" ",DEFAULT," ",getDefaultValueString(field.getType(), clnann.defaultRt()));
                }
            }
            if(!"".equals(clnann.check())){
                builder.adds(" ",CHECK,"(",clnann.check(),")");
            }
            if(!"".equals(clnann.comment())){
                builder.adds(" ",COMMENT," ","'",clnann.comment(),"'");
            }

        }
        return builder.done();
    }

    private static AppendUtil.AppendBuilder genCreateTableColumn(AppendUtil.AppendBuilder builder,Field field, DBColumn clnann){
        if(clnann==null){
            Class fieldType=field.getType();
            String colName=field.getName();
            String colType=getDbType(fieldType);
            builder.adds(colName,"\t",colType);
            return builder;
        }
        String name=getColumnName(field,clnann);
        String type=getColumnType(field,clnann);
        String restrict=getColumnRestrict(field,clnann);
        builder.adds(name,"\t",type," ",restrict);

        return builder;
    }

    public static List<Field> sortFields(Collection<Field> col){
        List<Field> ret=new ArrayList<>(col.size());
        ret.addAll(col);
        ret.sort(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                String name1=o1.getName();
                String name2=o2.getName();
                return name1.compareTo(name2);
            }
        });
        ret.sort(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                DBColumn ann1= AnnotationResolver.getFieldAnnotation(o1,false,false,false,DBColumn.class);
                DBColumn ann2=AnnotationResolver.getFieldAnnotation(o2,false,false,false,DBColumn.class);
                if(ann1!=null && ann2!=null){
                    return ann1.order()-ann2.order();
                }
                return 0;
            }
        });

        return ret;
    }


    public static String genCreateTableByBean(Class clazz){
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        DBTable tbann= AnnotationResolver.getClassAnnotation(clazz,false,false,DBTable.class);
        String table=getTableName(clazz,tbann);
        String schema=getSchemaName(clazz,tbann);
        long autoBeginNumber=-1;

        builder.adds("CREATE TABLE ");
        if(schema!=null && !"".equals(schema)){
            builder.addsLine(schema,".",table);
        }else{
            builder.addsLine(table);
        }
        builder.adds("(");

        Set<Field> fieldsSet= FieldResolver.getAllFields(clazz,true);
        List<Field> fields=sortFields(fieldsSet);
        boolean isFirst=true;
        for(Field item : fields){

            DBColumn clnann=AnnotationResolver.getFieldAnnotation(item,false,false,false,DBColumn.class);
            if(clnann!=null){
                if(clnann.ignore()){
                    continue;
                }
            }
            builder.adds("\n\t");
            if(!isFirst){
                builder.adds(",");
            }
            genCreateTableColumn(builder,item,clnann);
            if(clnann!=null){
                if(clnann.autoIncrement()){
                    autoBeginNumber= clnann.autoIncrementBeginNumber();;
                }
            }
            isFirst=false;
        }

        builder.line().adds(")");

        if(autoBeginNumber>=0){
            builder.adds(" ",AUTO_INCREMENT,"=",autoBeginNumber);
        }
        if(tbann!=null){
            if(!"".equals(tbann.comment())){
                builder.adds(" ",COMMENT," ","'",tbann.comment(),"'");
            }
        }

        builder.addsLine(";");

        return builder.done();
    }

    public static String genDropTableByBean(Class clazz,boolean isExists){
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        DBTable tbann= AnnotationResolver.getClassAnnotation(clazz,false,false,DBTable.class);
        String table=getTableName(clazz,tbann);
        String schema=getSchemaName(clazz,tbann);

        builder.adds("DROP TABLE ");
        if(isExists){
            builder.adds("IF EXISTS ");
        }
        if(schema!=null && !"".equals(schema)){
            builder.adds(schema,".",table);
        }else{
            builder.adds(table);
        }
        builder.adds(";");
        return builder.done();
    }

}
