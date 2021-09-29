package i2f.commons.core.utils.sql.generate.core;


import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.StringUtil;

import java.util.Map;

public class SqlPart {
    public static String like(String col,Object ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return "";
        }
        if(vals.length==1){
            return SqlBase.genKv(col,SqlBase.like(vals[0]));
        }
        StringBuffer buffer=new StringBuffer();
        for (int i = 0; i < vals.length; i++) {
            if(i==0){
                buffer.append(like(col,vals[i]));
            }else{
                buffer.append(" ");
                buffer.append(SqlBase.or(like(col,vals[i])));
            }
        }
        return buffer.toString();
    }
    public static String in(String col,Object ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return "";
        }
        StringBuffer buffer=new StringBuffer();
        for (int i = 0; i < vals.length; i++) {
            if(i!=0){
                buffer.append(SqlBase.SEP);
            }
            buffer.append(vals[i]);
        }
        return SqlBase.in(buffer.toString());
    }
    public static String condCk(String col,String ope,Object val){
        return SqlBase.genKvOpeCk(col,ope,val);
    }
    public static String cond(String col,String ope,Object val){
        return SqlBase.genKvOpe(col,ope,val);
    }
    public static String equCk(String col,Object val){
        return condCk(col,"=",val);
    }
    public static String equ(String col,Object val){
        return cond(col,"=",val);
    }
    public static String gt(String col,Object val){
        return cond(col,">",val);
    }
    public static String lt(String col,Object val){
        return cond(col,"<",val);
    }
    public static String gte(String col,Object val){
        return cond(col,">=",val);
    }
    public static String lte(String col,Object val){
        return cond(col,"<=",val);
    }
    public static String between(String col,Object min,Object max){
        return SqlBase.genBracket(comb(gte(col,min),"and",lte(col,max)));
    }
    public static String equs(Map<String,Object> kvs,String linker){
        return opes(kvs,"=",linker);
    }
    public static String opes(Map<String,Object> kvs,String ope,String linker){
        if(CheckUtil.isEmptyMap(kvs)){
            return "";
        }
        if(CheckUtil.isEmptyStr(linker,true)){
            linker="and";
        }
        StringBuffer buffer=new StringBuffer();
        int count=0;
        for(String key : kvs.keySet()){
            Object val=kvs.get(key);
            if(CheckUtil.isNull(val)){
                continue;
            }
            if(count!=0){
                buffer.append(SqlBase.genSp(linker));
            }
            buffer.append(SqlBase.genKvOpeCk(key,ope,val));
            count++;
        }
        return buffer.toString();
    }
    public static String[] colsVals(Map<String,Object> kvs){
        String[] ret=new String[]{"",""};
        if(CheckUtil.isEmptyMap(kvs)){
            return ret;
        }
        StringBuffer colsBuffer=new StringBuffer();
        StringBuffer valsBuffer=new StringBuffer();
        int count=0;
        for(String item : kvs.keySet()){
            Object val=kvs.get(item);
            if(CheckUtil.isNull(val)){
                continue;
            }
            if(count!=0){
                colsBuffer.append(SqlBase.SEP);
                valsBuffer.append(SqlBase.SEP);
            }
            colsBuffer.append(item);
            valsBuffer.append(SqlBase.genValCk(val));
            count++;
        }
        ret[0]=colsBuffer.toString();
        ret[1]=valsBuffer.toString();
        return ret;
    }
    public static String cols(String ... colNames){
        if(CheckUtil.isEmptyArray(colNames)){
            return "";
        }
        StringBuffer buffer=new StringBuffer();
        for (int i = 0; i < colNames.length; i++) {
            if(i!=0){
                buffer.append(SqlBase.SEP);
            }
            buffer.append(colNames[i]);
        }
        return buffer.toString();
    }
    public static String vals(Object ... colVals){
        if(CheckUtil.isEmptyArray(colVals)){
            return "";
        }
        StringBuffer buffer=new StringBuffer();
        for (int i = 0; i < colVals.length; i++) {
            if(i!=0){
                buffer.append(SqlBase.SEP);
            }
            buffer.append(SqlBase.genValCk(colVals[i]));
        }
        return buffer.toString();
    }
    public static String andEqus(Map<String,Object> kvs){
        return equs(kvs,"and");
    }
    public static String orEqus(Map<String,Object> kvs){
        return equs(kvs,"or");
    }
    public static String updEqus(Map<String,Object> kvs){
        return equs(kvs,",");
    }
    public static String where(String val){
        if(CheckUtil.isEmptyStr(val,true)){
            return "";
        }
        String[] parts=val.split("\\s+",1);
        if(CheckUtil.isEmptyArray(parts)){
            return SqlBase.where(val);
        }
        if("and".equalsIgnoreCase(parts[0])){
            return SqlBase.where(val.substring("and".length()));
        }
        if("or".equalsIgnoreCase(parts[0])){
            return SqlBase.where(val.substring("or".length()));
        }
        return SqlBase.where(val);
    }

    public static String andComb(String val1,String val2){
        return comb(val1,"and",val2);
    }
    public static String orComb(String val1,String val2){
        return comb(val1,"or",val2);
    }
    public static String comb(String val1, String linker, String val2){
        return StringUtil.combNotEmpty(true,linker,val1,val2);
    }
}
