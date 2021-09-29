package i2f.commons.core.utils.json;


import i2f.commons.core.utils.reflect.ReflectUtil;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class Json2 {
    public static boolean nullExclude=false;
    public static String sep=",";
    public static String quote="\"";
    public static String null2="null";
    public static SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    public static String toJson(Object obj){
        if(CheckUtil.isNull(obj)) {
            return whenNull(obj);
        }
        if(obj instanceof Boolean){
            return whenBoolean((Boolean)obj);
        }
        if(obj instanceof String){
            return whenString((String)obj);
        }
        if(obj instanceof Number){
            return whenNumber((Number)obj);
        }
        if(obj instanceof Date){
            return whenDate((Date)obj);
        }
        if(obj.getClass().isArray()){
            return whenArray(obj);
        }
        if(obj instanceof Map){
            return whenMap((Map)obj);
        }
        if(obj instanceof Collection){
            return whenCollection((Collection)obj);
        }

        return whenBean(obj);
    }
    private static String whenNull(Object obj){
        return null2;
    }
    private static String whenBoolean(Boolean obj){
        return String.valueOf(obj);
    }
    private static String whenString(String str){
        StringBuffer buffer=new StringBuffer();
        buffer.append(quote);
        char[] bufs=str.toCharArray();
        for(char ch : bufs){
            if(ch=='\"'){
                buffer.append("\\\"");
            }else if(ch=='\\'){
                buffer.append("\\\\");
            }else if(ch=='\b'){
                buffer.append("\\b");
            }else if(ch=='\f'){
                buffer.append("\\f");
            }else if(ch=='\n'){
                buffer.append("\\n");
            }else if(ch=='\r'){
                buffer.append("\\r");
            }else if(ch=='\t'){
                buffer.append("\\t");
            }else if(CheckUtil.isCh(ch)){
                buffer.append(ch);
            }else if(CheckUtil.isIn(ch,'&','=','<','>','\'')){
                buffer.append("\\u");
                String hex=Integer.toHexString((int)ch&0xffff);
                for(int i=hex.length();i<4;i++){
                    buffer.append('0');
                }
                buffer.append(hex);
            }else{
                buffer.append(ch);
            }
        }
        buffer.append(quote);
        return buffer.toString();
    }
    private static String whenNumber(Number num){
        return num.toString();
    }
    private static String whenDate(Date date){
        return AppendUtil.str(quote,fmt.format(date),quote);
    }
    private static String whenCollection(Collection col){
        StringBuffer buffer=new StringBuffer();
        buffer.append("[");
        if(CheckUtil.notNull(col)){
            boolean isFirst=true;
            for(Object item : col){
                if(nullExclude && CheckUtil.isNull(item)){
                    continue;
                }
                if(!isFirst){
                    buffer.append(sep);
                }
                buffer.append(toJson(item));
                isFirst=false;
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
    private static String whenArray(Object arr){
        if(CheckUtil.notNull(arr) && !arr.getClass().isArray()){
            return toJson(arr);
        }
        StringBuffer buffer=new StringBuffer();
        buffer.append("[");
        if(CheckUtil.notNull(arr)){
            int len=Array.getLength(arr);
            boolean isFirst=true;
            for(int i=0;i<len;i++){
                Object val=Array.get(arr,i);
                if(nullExclude && CheckUtil.isNull(val)){
                    continue;
                }
                if(!isFirst){
                    buffer.append(sep);
                }
                buffer.append(toJson(val));
                isFirst=false;
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
    private static String whenMap(Map map){
        AppendUtil.AppendBuilder buffer= AppendUtil.buffer();
        buffer.add("{");
        if(CheckUtil.notNull(map)){
            boolean isFirst=true;
            for(Object item : map.keySet()){
                Object val=map.get(item);
                if(nullExclude && CheckUtil.isNull(val)){
                    continue;
                }
                if(!isFirst){
                    buffer.add(sep);
                }
                buffer.adds(quote,item,quote,":",toJson(val));
                isFirst=false;
            }
        }
        buffer.add("}");
        return buffer.done();
    }
    private static String whenBean(Object obj){
        AppendUtil.AppendBuilder buffer= AppendUtil.buffer();
        buffer.add("{");
        if(CheckUtil.notNull(obj)){
            Class clazz=obj.getClass();
            Set<Field> fields= ReflectUtil.getForceAllFields(clazz,true);
            boolean isFirst=true;
            for(Field item : fields){
                String name=item.getName();
                Object val=ReflectUtil.getVal(obj,clazz,name,false);
                if(nullExclude && CheckUtil.isNull(val)){
                    continue;
                }
                if(!isFirst){
                    buffer.add(sep);
                }
                buffer.adds(quote,name,quote,":",toJson(val));
                isFirst=false;
            }
        }
        buffer.add("}");
        return buffer.done();
    }
}
