package i2f.commons.core.utils.sql.generate.core;


import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;

public class SqlBase {
    public static final String SEP=" , ";
    public static final String QUOTE="'";

    public static String select(String val){
        return genKv("select",val);
    }

    public static String update(String val){
        return genKv("update",val);
    }

    public static String delete(String val){
        return genKv("delete",val);
    }

    public static String insert(String val){
        return genKv("insert",val);
    }

    public static String into(String val){
        return genKv("into",val);
    }

    public static String values(String val){
        return genKv("values",genBracket(val));
    }

    public static String form(String val){
        return genKv("form",val);
    }

    public static String where(String val){
        return genKv("where",val);
    }

    public static String limit(String offset,String limit){
        return genKv("limit",genKvOpe(offset,",",limit));
    }

    public static String orderBy(String val){
        return genKv("order by",val);
    }

    public static String groupBy(String val){
        return genKv("group by",val);
    }

    public static String having(String val){
        return genKv("having",val);
    }

    public static String and(String val){
        return genKv("and",val);
    }

    public static String or(String val){
        return genKv("or",val);
    }

    public static String like(Object val){
        return genKv("like",getLikeVal(val));
    }

    public static String in(String val){
        return genKv("in",genBracket(val));
    }

    public static String genBracket(Object val){
        return AppendUtil.str(" ( ",val," ) ");
    }
    public static String genKvOpeCk(String name,String ope,Object val){
        if(val instanceof String) {
            return genKvOpeStr(name,ope,val);
        }else{
            return genKvOpe(name,ope,val);
        }
    }
    public static String genKvCk(String name,Object val){
        if(val instanceof String) {
            return genKvStr(name,val);
        }else{
            return genKv(name,val);
        }
    }
    public static String genValCk(Object val){
        if(val instanceof String) {
            return sqlStr(val);
        }else{
            return String.valueOf(val);
        }
    }
    public static String genKvOpeStr(String name,String ope,Object val){
        return AppendUtil.str(name," ",ope," ",sqlStr(val));
    }
    public static String genKvStr(String name,Object val){
        return AppendUtil.str(name," ",sqlStr(val));
    }
    public static String genKvOpe(String name,String ope,Object val){
        return AppendUtil.str(name," ",ope," ",val);
    }
    public static String genKv(String name,Object val){
        return AppendUtil.str(name," ",val);
    }
    public static String genSp(Object val){
        return AppendUtil.str(" ",val," ");
    }
    public static String sqlStr(Object val){
        return AppendUtil.str(QUOTE,getAntiSqlInjectStr(String.valueOf(val),false),QUOTE);
    }

    public static String getLikeVal(Object val){
        return AppendUtil.str("'%",getAntiSqlInjectStr(String.valueOf(val),false),"%'");
    }

    public static String getAntiSqlInjectStr(String str,boolean needTrim){
        if(CheckUtil.isNull(str)){
            return str;
        }
        str=str.replaceAll("\\'","''");
        if(needTrim){
            str=str.trim();
        }
        return str;
    }
}
