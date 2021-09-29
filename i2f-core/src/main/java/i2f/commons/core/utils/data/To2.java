package i2f.commons.core.utils.data;

import i2f.commons.core.utils.str.StringUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class To2 {
    public static<T> T[] arr(T ... objs){
        return DataUtil.arr(objs);
    }
    public static<T> T[] arr(Collection<T> col,Class<? extends T[]> toType){
        return DataUtil.toArr(col,toType);
    }
    public static <T,E> T[] arrCopy(int index, int len, Class<? extends T[]> tarType, E ... srcArr){
        return DataUtil.arrCopy(index,len,tarType,srcArr);
    }
    public static<T> T[] arrKey(Map<T,?> map, Class<? extends T[]> toType){
        return DataUtil.toArrKeys(map,toType);
    }
    public static<T> T[] arrVal(Map<?,T> map, Class<? extends T[]> toType){
        return DataUtil.toArrVals(map,toType);
    }
    public static String str(Object obj){
        return String.valueOf(obj);
    }

    public static<T> List<T> list(T ... vals){
        return DataUtil.toList(vals);
    }
    public static<T> List<T> listBase(List<T> saver,T ... vals){
        return DataUtil.toListBase(saver,vals);
    }

    public static<T> List<T> list(Collection<T> collection){
        return DataUtil.toList(collection);
    }
    public static<T> List<T> listBase(List<T> saver,Collection<T> collection){
        return DataUtil.toListBase(saver,collection);
    }

    public static Integer[] parseIntArr(String str,String regex){
        if(str==null){
            return new Integer[0];
        }
        String[] arr= StringUtil.split(str,regex,true);
        Vector<Integer> vec=new Vector<>();
        for(String item : arr){
            try {
                Integer num=Integer.parseInt(item);
                vec.add(num);
            }catch (NumberFormatException e){

            }
        }
        return DataUtil.toArr(vec,Integer[].class);
    }
    public static String[] parseKeyWords(String str){
        return parseKeywords(str,"\\s+");
    }
    public static String[] parseKeywords(String str,String regex){
        if(str==null){
            return new String[0];
        }
        String[] arr= StringUtil.split(str,regex,true);
        return arr;
    }

    public static Integer i(String num,Integer defVal){
        if(num==null){
            return defVal;
        }
        try{
            num=num.trim();
            return Integer.parseInt(num);
        }catch(NumberFormatException e){
            return defVal;
        }
    }
    public static Double d(String num,Double defVal){
        if(num==null){
            return defVal;
        }
        try{
            num=num.trim();
            return Double.parseDouble(num);
        }catch(NumberFormatException e){
            return defVal;
        }
    }
    public static Long l(String num,Long defVal){
        if(num==null){
            return defVal;
        }
        try{
            num=num.trim();
            return Long.parseLong(num);
        }catch(NumberFormatException e){
            return defVal;
        }
    }
    public static Float f(String num,Float defVal){
        if(num==null){
            return defVal;
        }
        try{
            num=num.trim();
            return Float.parseFloat(num);
        }catch(NumberFormatException e){
            return defVal;
        }
    }
    public static Boolean b(String bl,Boolean defVal){
        if("true".equalsIgnoreCase(bl)){
            return true;
        }else if("false".equalsIgnoreCase(bl)){
            return false;
        }
        return defVal;
    }

}
