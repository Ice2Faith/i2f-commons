package i2f.commons.core.utils.safe;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

public class CheckUtil {
    public static boolean isChLower(char ch) {
        return ch >= 'a' && ch <= 'z';
    }
    public static boolean isChUpper(char ch){
        return ch>='A' && ch<='Z';
    }
    public static boolean isCh(char ch){
        return isChLower(ch) || isChUpper(ch);
    }
    public static boolean isChNum(char ch){
        return ch>='0' && ch<='9';
    }
    public static boolean isChNum(char ch,int base){
        if(base<2 || base>(10+26)){
            return false;
        }
        if(base<=10){
            if(ch>='0' && ch<=('0'+base-1)){
                return true;
            }
        }else{
            if(isChNum(ch)){
                return true;
            }
            int off=base-10;
            if(ch>='A' && ch<=('A'+off-1)){
                return true;
            }
            if(ch>='a' && ch<=('a'+off-1)){
                return true;
            }
        }
        return false;
    }
    public static boolean isIn(Object tar, Object ... vals){
        if(isEmptyArray(vals)){
            return false;
        }
        for(Object item : vals){
            if(isNull(tar)){
                if(isNull(item)){
                    return true;
                }
            }else{
                if(tar.equals(item)){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean notIn(Object tar, Object ... vals){
        return !isIn(tar,vals);
    }
    public static boolean isExTrue(boolean  ... bools){
        if(isEmptyArray(bools)){
            return false;
        }
        for(boolean item : bools){
            if(item){
                return true;
            }
        }
        return false;
    }

    public static boolean isExFalse(boolean  ... bools){
        if(isEmptyArray(bools)){
            return false;
        }
        for(boolean item : bools){
            if(!item){
                return true;
            }
        }
        return false;
    }

    public static boolean isExNull(Object ... objs){
        if(isEmptyArray(objs)){
            return false;
        }
        for(Object item : objs){
            if(isNull(item)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNull(Object obj){
        return obj==null;
    }

    public static boolean isExEmptyStr(boolean needTrimed,String ... strs){
        if(isEmptyArray(strs)){
            return false;
        }
        for(String item : strs){
            if(isEmptyStr(item,needTrimed)){
                return true;
            }
        }
        return false;
    }
    /**
     * 检查是否是null或者是否是空串，根据需要进行trim之后比较是否空串
     * @param str 串
     * @param needTrimed 是否需要trim判断
     * @return
     */
    public static boolean isEmptyStr(String str, boolean needTrimed){
        if(isNull(str)) {
            return true;
        }
        if(needTrimed){
            return "".equals(str.trim());
        }
        return "".equals(str);
    }

    public static boolean isExEmptyArray(Object ... arrs){
        if(isEmptyArray(arrs)){
            return false;
        }
        for(Object item : arrs){
            if(isEmptyArray(item)){
                return true;
            }
        }
        return false;
    }

    public static<T> boolean isEmptyArray(T arr){
        if(notNull(arr) && !arr.getClass().isArray()){
            return false;
        }
        return (isNull(arr) || Array.getLength(arr)==0);
    }

    public static boolean isExEmptyCollection(Collection<?> ... collections){
        if(isEmptyArray(collections)){
            return false;
        }
        for(Collection<?> item : collections){
            if(isEmptyCollection(item)){
                return true;
            }
        }
        return false;
    }

    public static<T> boolean isEmptyCollection(Collection<T> collection){
        return (isNull(collection) || collection.size()==0);
    }

    public static boolean isExEmptyMap(Map<?,?> ... maps){
        if(isEmptyArray(maps)){
            return false;
        }
        for(Map<?,?> item : maps){
            if(isEmptyMap(item)){
                return true;
            }
        }
        return false;
    }

    public static<T,E> boolean isEmptyMap(Map<T,E> map){
        return (isNull(map) || map.size()==0);
    }
    public static boolean notNull(Object obj){
        return !isNull(obj);
    }

    public static boolean notEmptyStr(String str, boolean needTrimed){
        return !isEmptyStr(str,needTrimed);
    }

    public static<T> boolean notEmptyArray(T[] arr){
        return !isEmptyArray(arr);
    }

    public static<T> boolean notEmptyCollection(Collection<T> collection){
        return !isEmptyCollection(collection);
    }
    public static<T,E> boolean notEmptyMap(Map<T,E> map){
        return !isEmptyMap(map);
    }

    public static boolean isExEmpty(Object ... objs){
        if(isEmptyArray(objs)){
            return false;
        }
        for(Object item : objs){
            if(isEmpty(item)){
                return true;
            }
        }
        return false;
    }

    public static<T> boolean isEmpty(T obj){
        if(notNull(obj) && obj.getClass().isArray()){
            return isEmptyArray(obj);
        }else if(obj instanceof Map<?,?>){
            return isEmptyMap((Map) obj);
        } else if(obj instanceof Collection<?>){
            return isEmptyCollection((Collection) obj);
        }else if(obj instanceof String){
            return isEmptyStr((String)obj,false);
        }
        return isNull(obj);
    }

    private static boolean isNumOpe(Number num1,String ope,Number num2){
        BigDecimal bnum1=(num1 instanceof BigDecimal?(BigDecimal)num1: new BigDecimal(num1.toString()));
        BigDecimal bnum2=(num2 instanceof BigDecimal?(BigDecimal)num2: new BigDecimal(num2.toString()));
        if("==".equals(ope)){
            return bnum1.compareTo(bnum2)==0;
        }else if(">".equals(ope)){
            return bnum1.compareTo(bnum2)>0;
        }else if(">=".equals(ope)){
            return bnum1.compareTo(bnum2)>=0;
        }else if("<".equals(ope)){
            return bnum1.compareTo(bnum2)<0;
        }else if("<=".equals(ope)){
            return bnum1.compareTo(bnum2)<=0;
        }else if("!=".equals(ope)){
            return bnum1.compareTo(bnum2)!=0;
        }
        return false;
    }

    public static boolean isNumLower(Number targetNum, Number cmpNum){
        return isNumOpe(targetNum,"<",cmpNum);
    }

    public static boolean isExNumLower(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumLower(item,cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumGather(Number targetNum, Number cmpNum){
        return isNumOpe(targetNum,">",cmpNum);
    }

    public static boolean isExNumGather(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumGather(item,cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumLowerEqu(Number targetNum,Number cmpNum){
        return isNumOpe(targetNum,"<=",cmpNum);
    }

    public static boolean isExNumLowerEqu(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumLowerEqu(item,cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumGatherEqu( Number targetNum,Number cmpNum){
        return isNumOpe(targetNum,">=",cmpNum);
    }

    public static boolean isExNumGatherEqu(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumGatherEqu(item,cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumBetweenBoth(Number targetNum,Number min,Number max){
        return isNumGatherEqu(targetNum,min) && isNumLowerEqu(targetNum,max);
    }

    public static boolean isExNumBetweenBoth(Number min,Number max, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumBetweenBoth(item,min,max)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumBetweenLeft(Number targetNum,Number min,Number max){
        return isNumGatherEqu(targetNum,min) && isNumLower(targetNum,max);
    }

    public static boolean isExNumBetweenLeft(Number min,Number max, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumBetweenLeft(item,min,max)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumBetweenOpen(Number targetNum,Number min,Number max){
        return isNumGather(targetNum,min) && isNumLower(targetNum,max);
    }

    public static boolean isExNumBetweenOpen(Number min,Number max, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumBetweenOpen(item,min,max)){
                return true;
            }
        }
        return false;
    }

    public static boolean isExNumEqu(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumOpe(item,"==",cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isExNumNotEqu(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumOpe(item,"!=",cmpNum)){
                return true;
            }
        }
        return false;
    }

    //一些常用的正则表达式串，这些串都可以作为参数进行匹配
    public static final String REGEX_INT_NUMBER="^[+|-]?[1-9]\\d*$";
    public static final String REGEX_FLOAT_NUMBER="^[+|-]?[1-9]\\d*(\\.\\d+)?|[+|-]?0(\\.\\d+)?$";
    public static final String REGEX_SCIENTIFIC_NUMBER="^[+|-]?[1-9]\\d*(\\.\\d+)?([E|e][+|-]?[1-9]\\d*)|[+|-]?0(\\.\\d+)?([E|e][+|-]?[1-9]\\d*)$";
    public static final String REGEX_MOBILE_11="^(\\+[0-9]{2}\\s*)?[1][0-9]{10}$";
    public static final String REGEX_EMAIL="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String REGEX_ID_NUMBER="^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
    public static final String REGEX_URL="^[a-zA-Z]+://[^\\s]*$";
    //允许匹配的日期时间格式
    //1-1-1 23:59:59 666
    //9999-12-31 00:00:00 000
    //2020年08月06日12时35分16秒3豪
    //所有位置均为1个字符占位即可
    //段内分隔符不允许空白符
    //段间分隔允许空白符
    //yyyy-MM-dd
    public static final String REGEX_DATE="^[1-9]([0-9]{0,3})?([\\S|\\D])(0?[1-9]|1[0-2])([\\S|\\D])(0?[1-9]|[1-2][0-9]|3[0-1])$";
    //HH:mm:ss SSS
    //HH:mm:ss
    public static final String REGEX_TIME="^(0?[0-9]|1[0-9]|2[0-3])([\\S|\\D])[0-5]?[0-9]([\\S|\\D])[0-5]?[0-9](([\\D])[0-9]{0,3})?$";
    //yyyy-MM-dd HH:mm:ss SSS
    //yyyy-MM-dd HH:mm:ss
    public static final String REGEX_DATE_TIME="^[1-9]([0-9]{0,3})?([\\S|\\D])(0?[1-9]|1[0-2])([\\S|\\D])(0?[1-9]|[1-2][0-9]|3[0-1])([\\D])(0?[0-9]|1[0-9]|2[0-3])([\\S|\\D])[0-5]?[0-9]([\\S|\\D])[0-5]?[0-9](([\\D])[0-9]{0,3})?$";
    /**
     * 匹配字符串，通过正则
     * @param str 字符串
     * @param regex 正则
     * @return 是否满足正则
     */
    public static boolean isMatched(String str,String regex){
        if(isExNull(str,regex)){
            return false;
        }
        return str.matches(regex);
    }
    public static boolean exNotMatched(String regex,String ... strs){
        if(isEmptyArray(strs)){
            return false;
        }
        for(String item : strs){
            if(notMatched(item,regex)){
                return true;
            }
        }
        return false;
    }

    public static boolean notMatched(String str,String regex){
        return !isMatched(str,regex);
    }
    public static boolean isIntNumber(String str){
        return isMatched(str,REGEX_INT_NUMBER);
    }
    public static boolean isFloatNumber(String str){
        return isMatched(str,REGEX_FLOAT_NUMBER);
    }
    public static boolean isScientificNumber(String str){
        return isMatched(str,REGEX_SCIENTIFIC_NUMBER);
    }
    public static boolean isPhoneNumber(String str){
        return isMatched(str,REGEX_MOBILE_11);
    }
    public static boolean isEmailAddr(String str){
        return isMatched(str,REGEX_EMAIL);
    }

    public static boolean isEquals(boolean deep,Object obj1,Object obj2){
        if(obj1==obj2){
            return true;
        }
        if(isExNull(obj1,obj2)){
            return false;
        }
        if(deep){
            return isDeepEquals(obj1, obj2);
        }
        return obj1.equals(obj2);
    }
    public static boolean isDeepEquals(Object obj1,Object obj2){
        Class clazzObj1=obj1.getClass();
        Class clazzObj2=obj2.getClass();
        if(obj1 instanceof Map && obj2 instanceof Map){
            return deepEqualsMap((Map)obj1,(Map)obj2);
        }
        if(obj1 instanceof Collection && obj2 instanceof Collection){
            return deepEqualsCollection((Collection)obj1,(Collection)obj2);
        }
        if(clazzObj1.isArray() && clazzObj2.isArray()){
            return deepEqualsArray(obj1, obj2);
        }
        return obj1.equals(obj2);
    }
    public static boolean deepEqualsCollection(Collection col1,Collection col2){
        if(col1.size()!=col2.size()){
            return false;
        }
        Iterator it1=col1.iterator();
        Iterator it2=col2.iterator();
        while (it1.hasNext()){
            Object o1=it1.next();
            Object o2=it2.next();
            boolean eqs=isDeepEquals(o1,o2);
            if(!eqs){
                return false;
            }
        }
        return true;
    }
    public static boolean deepEqualsMap(Map map1,Map map2){
        if(map1.size()!=map2.size()){
            return false;
        }
        Set st1= map1.keySet();
        Set st2= map2.keySet();
        if(!deepEqualsCollection(st1,st2)){
            return false;
        }
        for(Object item : st1){
            Object o1=map1.get(item);
            Object o2=map2.get(item);
            boolean eqs=isDeepEquals(o1,o2);
            if(!eqs){
                return false;
            }
        }
        return true;
    }
    public static boolean deepEqualsArray(Object obj1,Object obj2){
        Class clazzObj1=obj1.getClass();
        Class clazzObj2=obj2.getClass();
        if(clazzObj1.isArray() && clazzObj2.isArray()){
            int len=Array.getLength(obj1);
            if(len!=Array.getLength(obj2)){
                return false;
            }
            for (int i = 0; i < len; i++) {
                Object p1=Array.get(obj1,i);
                Object p2=Array.get(obj2,i);
                boolean eqs=isDeepEquals(p1,p2);
                if(!eqs){
                    return false;
                }
            }
            return true;
        }
        return isDeepEquals(obj1, obj2);
    }
}
