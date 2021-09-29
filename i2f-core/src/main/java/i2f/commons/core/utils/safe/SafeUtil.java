package i2f.commons.core.utils.safe;

public class SafeUtil {
    public static <T> T nullCk2(T obj,T defVal){
        if(CheckUtil.isNull(obj)){
            return defVal;
        }
        return obj;
    }
    public static Object null2(Object obj,Object defVal){
        if(CheckUtil.isNull(obj)){
            return defVal;
        }
        return obj;
    }
    public static<T> Object whenCk2(T obj,boolean condition,T defVal){
        if(condition){
            return defVal;
        }
        return obj;
    }
    public static Object when2(Object obj,boolean condition,Object defVal){
        if(condition){
            return defVal;
        }
        return obj;
    }
    public static String notNullStr(String str){
        return notNullStr(str,false);
    }
    public static String notNullStr(String str,boolean needTrim){
        if(CheckUtil.isNull(str)){
            return "";
        }
        if(needTrim){
            return str.trim();
        }
        return str;
    }

    public static String emptyStr2(String str,boolean needTrim,String defVal){
        if(CheckUtil.isEmptyStr(str,needTrim)){
            return defVal;
        }
        return str;
    }

}
