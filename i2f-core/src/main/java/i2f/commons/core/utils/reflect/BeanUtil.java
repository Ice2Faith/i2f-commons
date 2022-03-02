package i2f.commons.core.utils.reflect;

import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/2/16 17:43
 * @desc
 */
public class BeanUtil {
    protected static volatile ConcurrentHashMap<Class,Map<String,Field>> cacheFields=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Set<String>> cacheFieldsByGetterSetter=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Set<Method>> cacheMethods=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Set<Method>> cacheGetters=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Set<Method>> cacheSetters=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Set<Constructor>> cacheConstructors=new ConcurrentHashMap<>();

    /**
     * 将实体中的所有空字符串转变为null
     * 支持是否先trim再判断是否空串
     * @param obj
     * @param trim
     */
    public static void empty2null(Object obj,boolean trim){
        if(CheckUtil.isNull(obj)){
            return;
        }
        Class clazz=obj.getClass();
        Map<String,Field> fds=fields(clazz);
        for(String item : fds.keySet()){
            Object val=get(obj,item);
            if(val!=null){
                if(val instanceof String){
                    String str=(String)val;
                    if(trim){
                        str=str.trim();
                    }
                    if("".equals(str)){
                        set(obj,item,null);
                    }
                }
            }
        }
    }

    public static List<Map<String, Object>> camelKey(List<Map<String, Object>> list){
        return camelKey(list,false);
    }
    /**
     * 将Map的Key全部变为驼峰
     * @param list
     * @return
     */
    public static List<Map<String, Object>> camelKey(List<Map<String, Object>> list,boolean stringifyNumber){
        if(CheckUtil.isNull(list)){
            return list;
        }
        List<Map<String, Object>> ret=new ArrayList<>(list.size());
        for(Map<String, Object> item : list){
            ret.add(camelKey(item,stringifyNumber));
        }
        return ret;
    }

    public static Map<String, Object> camelKey(Map<String,Object> map) {
        return camelKey(map,false);
    }
    /**
     * 将Map的Key全部变为驼峰
     * @param map
     * @return
     */
    public static Map<String, Object> camelKey(Map<String,Object> map,boolean stringifyNumber){
        if(CheckUtil.isNull(map)){
            return map;
        }
        Map<String, Object> ret=new HashMap<>();
        for(Map.Entry<String, Object> item : map.entrySet()){
            Object obj=item.getValue();
            if(obj!=null && stringifyNumber){
                Class type=obj.getClass();
                boolean isnum=isTargetType(type,int.class,Integer.class,
                        short.class,Short.class,
                        long.class,Long.class,
                        byte.class,Byte.class,
                        BigInteger.class,BigDecimal.class);
                if(isnum){
                    obj=String.valueOf(obj);
                }
            }
            ret.put(StringUtil.toCamel(item.getKey()),obj);
        }
        return ret;
    }

    /**
     * 实体列表转换为Map列表，支持是否保留null
     * @param list
     * @param keepNull
     * @return
     */
    public static List<Map<String,Object>> toMapList(List list,boolean keepNull){
        if(CheckUtil.isNull(list)){
            return null;
        }
        List<Map<String,Object>> ret=new ArrayList<>();
        for(Object item : list){
            Map<String,Object> map=toMap(item,keepNull);
            ret.add(map);
        }
        return ret;
    }

    /**
     * 实体转换为Map，支持是否保留null
     * @param obj
     * @param keepNull
     * @return
     */
    public static Map<String,Object> toMap(Object obj,boolean keepNull){
        if(CheckUtil.isNull(obj)){
            return null;
        }
        Map<String,Object> map=new HashMap<>();
        Class clazz=obj.getClass();
        Map<String,Field> fds=fields(clazz);
        for(Map.Entry<String,Field> item : fds.entrySet()){
            String name=item.getKey();
            Object val=get(obj,name);
            if(val==null && !keepNull){
                continue;
            }
            map.put(name,val);
        }
        return map;
    }

    /**
     * Map的列表转换为实体列表
     * @param list
     * @param clazz
     * @param camel
     * @param <T>
     * @return
     */
    public static<T> List<T> formMapList(List<Map<String,Object>> list,Class<T> clazz,boolean camel){
        List<T> ret=new ArrayList<>();
        for(Map<String,Object> item : list){
            T val=formMap(item,clazz,camel);
            ret.add(val);
        }
        return ret;
    }

    /**
     * Map的列表转换为实体列表
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static<T> List<T> formMapList(List<Map<String,Object>> list,Class<T> clazz){
        List<T> ret=new ArrayList<>();
        for(Map<String,Object> item : list){
            T val=formMap(item,clazz);
        }
        return ret;
    }

    /**
     * 实例化指定类
     * @param clazz
     * @param <T>
     * @return
     */
    public static<T> T instance(Class<T> clazz){
        Set<Constructor> cons=constructors(clazz);
        for(Constructor item : cons){
            if(item.getParameterCount()==0){
                try{
                    return (T)item.newInstance();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        try{
            return (T)clazz.newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Map转换为指定的类，支持是否Map的key转为驼峰后匹配
     * @param map
     * @param clazz
     * @param camel
     * @param <T>
     * @return
     */
    public static<T> T formMap(Map<String,Object> map,Class<T> clazz,boolean camel){
        T ins= instance(clazz);
        formMap(map,ins,camel);
        return ins;
    }

    /**
     * 将Map转换为指定的类，直接匹配
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static<T> T formMap(Map<String,Object> map,Class<T> clazz){
        T ins= instance(clazz);
        formMap(map,ins);
        return ins;
    }

    /**
     * 将Map转换为指定的类，支直接匹配
     * @param map
     * @param obj
     */
    public static void formMap(Map<String,Object> map,Object obj){
        formMap(map,obj,false);
    }

    /**
     * 将Map转换为指定的类，支持是否Map的key转为驼峰后匹配
     * @param map
     * @param obj
     * @param camel
     */
    public static void formMap(Map<String,Object> map,Object obj,boolean camel){
        if(CheckUtil.isExNull(map,obj)){
            return;
        }
        for(Map.Entry<String,Object> item : map.entrySet()){
            String key=item.getKey();
            if(camel){
                key=StringUtil.toCamel(key);
            }
            set(obj,key,item.getValue());
        }
    }

    /**
     * 对象属性对拷，需求两个对象的字段名具有同名字段
     * @param srcObj
     * @param dstObj
     */
    public static void copy(Object srcObj,Object dstObj){
        if(CheckUtil.isExNull(srcObj,dstObj)){
            return;
        }
        Class srcCls=srcObj.getClass();
        Class dstCls=dstObj.getClass();
        Map<String,Field> srcFields=fields(srcCls);
        Map<String,Field> dstFields=fields(dstCls);
        for(Map.Entry<String,Field> sitem : srcFields.entrySet()){
            String sname=sitem.getKey();
            for(Map.Entry<String,Field> ditem : dstFields.entrySet()){
                if(sname.equals(ditem.getKey())){
                    Object val=get(srcObj,sname);
                    set(dstObj,sname,val);
                    break;
                }
            }
        }
    }

    /**
     * 获取对象的某个字段值
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object get(Object obj,String fieldName){
        if(CheckUtil.isNull(obj)){
            return null;
        }
        if(CheckUtil.isEmpty(fieldName)){
            return null;
        }
        Class clazz=obj.getClass();

        Map<String,Field> fds=fields(clazz);
        if(fds.containsKey(fieldName)){
            Field fd=fds.get(fieldName);
            if(fd==null){
                Set<Method> gts=getters(clazz);
                String getterName="get"+StringUtil.firstUpperCase(fieldName);
                String isName="is"+StringUtil.firstUpperCase(fieldName);
                for(Method item : gts){
                    if(getterName.equals(item.getName()) || isName.equals(item.getName())){
                        try{
                            return item.invoke(obj);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                try{
                    fd.setAccessible(true);
                    return fd.get(obj);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 设置对象的某个字段值
     * @param obj
     * @param fieldName
     * @param val
     */
    public static void set(Object obj,String fieldName,Object val){
        if(CheckUtil.isNull(obj)){
            return;
        }
        if(CheckUtil.isEmpty(fieldName)){
            return;
        }
        Class clazz=obj.getClass();

        Map<String,Field> fds=fields(clazz);
        if(fds.containsKey(fieldName)){
            Field fd=fds.get(fieldName);
            if(fd==null){
                Set<Method> gts=setters(clazz);
                String getterName="set"+StringUtil.firstUpperCase(fieldName);
                for(Method item : gts){
                    if(getterName.equals(item.getName())){
                        try{
                            Class type=item.getParameterTypes()[0];
                            Object sval=val;
                            if (CheckUtil.isNull(val) || canConvert2(type, val)) {
                                sval=tryConvertType(type,val);
                            }
                            item.invoke(obj,sval);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                try{
                    Class type=fd.getType();
                    Object sval=val;
                    if (CheckUtil.isNull(val) || canConvert2(type, val)) {
                        sval=tryConvertType(type,val);
                    }
                    fd.setAccessible(true);
                    fd.set(obj,sval);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取指定类的所有字段，包含具有get/set方法的字段
     * @param clazz
     * @return
     */
    public static Map<String, Field> fields(Class clazz){
        Map<String, Field> ret=new HashMap<>();
        if(CheckUtil.isNull(clazz)){
            return ret;
        }
        if(cacheFields.containsKey(clazz)){
            return cacheFields.get(clazz);
        }
        Set<Field> set=mergeElement(clazz.getDeclaredFields(),clazz.getFields());
        for(Field item : set){
            ret.put(item.getName(),item);
        }
        Set<String> gsFields=fieldsFromGetterSetter(clazz);
        for(String item : gsFields){
            ret.put(item,null);
        }
        cacheFields.put(clazz,ret);
        return ret;
    }

    /**
     * 获取指定类的具有get/set方法的所有字段
     * @param clazz
     * @return
     */
    public static Set<String> fieldsFromGetterSetter(Class clazz){
        if(cacheFieldsByGetterSetter.containsKey(clazz)){
            return cacheFieldsByGetterSetter.get(clazz);
        }
        Set<String> ret=new HashSet<>();
        Set<Method> gtrs=getters(clazz);
        Set<Method> strs=setters(clazz);
        for(Method item : gtrs){
            String name=item.getName();
            String fieldName="";
            if(name.startsWith("is")){
                fieldName=name.substring("is".length());
            }else{
                fieldName=name.substring("get".length());
            }
            String setName="set"+fieldName;
            for(Method sitem : strs){
                if(sitem.getName().equals(setName)){
                    ret.add(StringUtil.firstLowerCase(fieldName));
                    break;
                }
            }
        }
        cacheFieldsByGetterSetter.put(clazz,ret);
        return ret;
    }

    /**
     * 获取指定类的所有getter
     * @param clazz
     * @return
     */
    public static Set<Method> getters(Class clazz){
        if(cacheGetters.containsKey(clazz)){
            return cacheGetters.get(clazz);
        }
        Set<Method> ret=new HashSet<>();
        Set<Method> map=methods(clazz);
        for(Method item : map){
            String name=item.getName();
            if(item.getParameterCount()==0
                    &&!item.getReturnType().equals(Void.TYPE)
                    && (name.startsWith("get") || name.startsWith("is"))){
                ret.add(item);
            }
        }
        cacheGetters.put(clazz,ret);
        return ret;
    }

    /**
     * 获取指定类的所有setter
     * @param clazz
     * @return
     */
    public static Set<Method> setters(Class clazz){
        if(cacheSetters.containsKey(clazz)){
            return cacheSetters.get(clazz);
        }
        Set<Method> ret=new HashSet<>();
        Set<Method> map=methods(clazz);
        for(Method item : map){
            String name=item.getName();
            if(item.getParameterCount()==1
                    &&item.getReturnType().equals(Void.TYPE)
                    && name.startsWith("set")){
                ret.add(item);
            }
        }
        cacheSetters.put(clazz,ret);
        return ret;
    }

    /**
     * 获取指定类的所有方法
     * @param clazz
     * @return
     */
    public static Set<Method> methods(Class clazz){
        Set<Method> ret=new HashSet<>();
        if(CheckUtil.isNull(clazz)){
            return ret;
        }
        if(cacheMethods.containsKey(clazz)){
            return cacheMethods.get(clazz);
        }
        ret=mergeElement(clazz.getDeclaredMethods(),clazz.getMethods());
        cacheMethods.put(clazz,ret);
        return ret;
    }

    /**
     * 获取指定类的所有构造方法
     * @param clazz
     * @return
     */
    public static Set<Constructor> constructors(Class clazz){
        Set<Constructor> ret=new HashSet<>();
        if(CheckUtil.isNull(clazz)){
            return ret;
        }
        if(cacheConstructors.containsKey(clazz)){
            return cacheConstructors.get(clazz);
        }

        ret=mergeElement(clazz.getConstructors());

        cacheConstructors.put(clazz,ret);
        return ret;
    }

    /**
     * 合并多个数组为一个唯一的set
     * @param arr
     * @param <T>
     * @return
     */
    public static<T> Set<T> mergeElement(T[] ... arr){
        Set<T> set=new HashSet<>();
        for(T[] item : arr){
            for(T elem : item){
                set.add(elem);
            }
        }
        return set;
    }

    public static <E> boolean canConvert2(Class dstType, E srcObj) {
        if (srcObj == null) {
            return true;
        }
        if (isTargetType(dstType, Object.class)) {
            return true;
        }
        Class srcType = srcObj.getClass();
        if (isSameType(dstType, srcType)) {
            return true;
        }
        if (isTargetType(dstType, String.class)) {
            return true;
        }
        if (isTargetType(dstType, Date.class, java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class)
                && isTargetType(srcType, Date.class, java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class)) {
            return true;
        }

        if (isTargetType(dstType, byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                char.class, Character.class,
                BigInteger.class)
                && isTargetType(srcType, byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                char.class, Character.class,
                BigInteger.class)) {
            return true;
        }

        if (isTargetType(dstType, float.class, Float.class,
                double.class, Double.class,
                BigDecimal.class)
                && isTargetType(srcType, float.class, Float.class,
                double.class, Double.class,
                BigDecimal.class)) {
            return true;
        }

        if (isTargetType(dstType, boolean.class, Boolean.class)
                && isTargetType(srcType, boolean.class, Boolean.class, String.class)) {
            return true;
        }


        return false;
    }

    public static <E> Object tryConvertType(Class<E> dstType, Object srcObj) {
        if (srcObj == null) {
            return srcObj;
        }
        if (isTargetType(dstType, Object.class)) {
            return srcObj;
        }
        Class srcType = srcObj.getClass();
        if (isSameType(dstType, srcType)) {
            return srcObj;
        }
        if (isTargetType(dstType, String.class)) {
            return String.valueOf(srcObj);
        }
        if (isTargetType(dstType, Date.class, java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class)
                && isTargetType(srcType, Date.class, java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class)) {
            long timest = ((Date) srcObj).getTime();
            if (isTargetType(dstType, Date.class)) {
                return new Date(timest);
            } else if (isTargetType(dstType, java.sql.Timestamp.class)) {
                return new java.sql.Timestamp(timest);
            } else if (isTargetType(dstType, java.sql.Date.class)) {
                return new java.sql.Date(timest);
            } else if (isTargetType(dstType, java.sql.Time.class)) {
                return new java.sql.Time(timest);
            }
        }

        if (isTargetType(dstType, byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                char.class, Character.class,
                BigInteger.class)
                && isTargetType(srcType, byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                char.class, Character.class,
                BigInteger.class)) {
            BigInteger val = null;
            if (isTargetType(srcType, Character.class, char.class)) {
                int cval = (int) (char) (Character) srcObj;
                val = new BigInteger(String.valueOf(cval));
            } else {
                val = new BigInteger(String.valueOf(srcObj));
            }

            if (isTargetType(dstType, byte.class, Byte.class)) {
                return val.byteValue();
            } else if (isTargetType(dstType, short.class, Short.class)) {
                return val.shortValue();
            } else if (isTargetType(dstType, int.class, Integer.class)) {
                return val.intValue();
            } else if (isTargetType(dstType, long.class, Long.class)) {
                return val.longValue();
            } else if (isTargetType(dstType, char.class, Character.class)) {
                return (char) val.intValue();
            } else if (isTargetType(dstType, BigInteger.class)) {
                return val;
            }
            return srcObj;
        }

        if (isTargetType(dstType, float.class, Float.class,
                double.class, Double.class,
                BigDecimal.class)
                && isTargetType(srcType, float.class, Float.class,
                double.class, Double.class,
                BigDecimal.class)) {
            BigDecimal val = new BigDecimal(String.valueOf(srcObj));
            if (isTargetType(dstType, float.class, Float.class)) {
                return val.floatValue();
            } else if (isTargetType(dstType, double.class, Double.class)) {
                return val.doubleValue();
            } else if (isTargetType(dstType, BigDecimal.class)) {
                return val;
            }
            return srcObj;
        }

        return srcObj;
    }

    private static Class[][] baseTypeMapping = {
            {byte.class, Byte.class},
            {short.class, Short.class},
            {int.class, Integer.class},
            {long.class, Long.class},
            {float.class, Float.class},
            {double.class, Double.class},
            {char.class, Character.class},
            {boolean.class, Boolean.class},
    };


    public static boolean isSameType(Class<?> t1, Class<?> t2) {
        boolean ret = false;
        if (t1 == t2) {
            ret = true;
        } else if (CheckUtil.isExNull(t1, t2)) {
            ret = false;
        } else if (t1.equals(t2)) {
            ret = true;
        } else {
            for (int i = 0; i < baseTypeMapping.length; i++) {
                if (baseTypeMapping[i][0].equals(t1) && baseTypeMapping[i][1].equals(t2)) {
                    ret = true;
                    break;
                }
                if (baseTypeMapping[i][1].equals(t1) && baseTypeMapping[i][0].equals(t2)) {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    public static boolean isTargetType(Class ckType,Class ... tarTypes){
        if(CheckUtil.isEmptyArray(tarTypes)){
            return false;
        }
        for(Class item : tarTypes){
            if(ckType.equals(item)){
                return true;
            }
        }
        return false;
    }
}
