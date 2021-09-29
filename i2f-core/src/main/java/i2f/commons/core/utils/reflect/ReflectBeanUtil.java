package i2f.commons.core.utils.reflect;


import i2f.commons.core.utils.reflect.annotations.ConvertorBean2Map;
import i2f.commons.core.utils.reflect.annotations.ConvertorMap2Bean;
import i2f.commons.core.utils.reflect.core.Bean2MapFieldConvertor;
import i2f.commons.core.utils.reflect.core.Map2BeanFieldConvertor;
import i2f.commons.core.utils.reflect.core.resolver.BeanAnnotationResolver;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ReflectBeanUtil {
    public static <T> Map<Field,Object> getForeignKeyFieldAndForVal(T obj){
        return BeanAnnotationResolver.getForeignKeyFieldAndForVal(obj);
    }
    public static <T> T beanFieldsNull2Vals(T obj,boolean ignoreCase,String[] fieldNames,Object ... vals){
        return BeanResolver.fieldsNull2Vals(obj, ignoreCase, fieldNames, vals);
    }
    public static <T> List<T> beanListFields2Value(List<T> list,Object val,boolean ignoreCase,String fieldNames){
        return BeanResolver.listFields2Value(list, val, ignoreCase, fieldNames);
    }

    public static <T> T beanFields2Null(T obj,boolean ignoreCase,String ... fieldNames){
        return BeanResolver.fields2Null(obj, ignoreCase, fieldNames);
    }
    public static <T> T beanFields2Value(T obj,Object val,boolean ignoreCase,String ... fieldNames){
        return BeanResolver.fields2Value(obj, val, ignoreCase, fieldNames);
    }


    public static <T> String bean2String(T obj,boolean forceAllAttr){
        return BeanResolver.stringify(obj,forceAllAttr);
    }
    public static<T,E> void beanCopy(T from,E to,boolean ignoreCase){
        BeanResolver.copy(from,to,ignoreCase);
    }
   public static List<Map<String,Object>> beanList2MapList(List<Object> beans, boolean removeNull){
        return BeanResolver.toMapList(beans, removeNull);
    }

    public static Map<String,Object> bean2Map(Object obj,boolean removeNull){
        return BeanResolver.toMap(obj, removeNull);
    }
    public static Map<String,Object> bean2Map(Object obj,boolean removeNull,boolean ckIgnoreAnnotation){
        return BeanResolver.toMap(obj, removeNull, ckIgnoreAnnotation);
    }
    public static List<Map<String,Object>> beanList2MapList(List<Object> beans, boolean removeNull,boolean ckIgnoreAnnotation){
        return BeanResolver.toMapList(beans, removeNull, ckIgnoreAnnotation);
    }
    public static List<Map<String,Object>> beanList2MapList(List<Object> beans, boolean removeNull,boolean ckIgnoreAnnotation,boolean useAliasAnnotations){
        return BeanResolver.toMapList(beans, removeNull, ckIgnoreAnnotation, useAliasAnnotations);
    }

    public static Map<String,Object> bean2Map(Object obj,boolean removeNull,boolean ckIgnoreAnnotation,boolean useAliasAnnotations){
        return BeanResolver.toMap(obj, removeNull, ckIgnoreAnnotation, useAliasAnnotations);
    }

    public static<T> String[] getBeanFieldsColumnNames(Class<T> clazz,boolean ckIgnoreAnnotation,boolean useAliasAnnotations){
        return BeanResolver.getBeanFieldsColumnNames(clazz,ckIgnoreAnnotation,useAliasAnnotations);
    }

    public static boolean isIgnoreBean2MapField(Field item){
        return BeanResolver.isIgnoreBean2MapField(item);
    }

    public static boolean isIgnoreMap2BeanField(Field item){
        return BeanResolver.isIgnoreMap2BeanField(item);
    }

    public static boolean isIgnoreBothField(Field item){
        return BeanResolver.isIgnoreBothField(item);
    }

    public static String getBeanFieldColumnName(Field field){
        return BeanResolver.getBeanFieldColumnName(field);
    }

    public static String getBeanTableName(Class clazz){
        return BeanAnnotationResolver.getTableName(clazz);
    }

    public static boolean isBeanPrimaryKeyField(Field field){
        return BeanAnnotationResolver.isBeanPrimaryKeyField(field);
    }

    public static boolean containsBeanPrimaryKey(Class clazz){
        return BeanAnnotationResolver.containsBeanPrimaryKey(clazz);
    }
    public static boolean isBeanAutoPrimaryKey(Field field){
        return BeanAnnotationResolver.isBeanAutoPrimaryKey(field);
    }
    public static Field getBeanPrimaryKeyField(Class clazz){
        return BeanAnnotationResolver.getBeanPrimaryKeyField(clazz);
    }
    public static String getBeanPrimaryKeyColumnName(Class clazz){
        return BeanAnnotationResolver.getBeanPrimaryKeyColumnName(clazz);
    }
    public static <T> String getBeanPrimaryKeyColumnName(T obj){
        return BeanAnnotationResolver.getBeanPrimaryKeyColumnName(obj);
    }
    public static<T,E> E getBeanPrimaryKeyValue(T obj){
        return BeanAnnotationResolver.getBeanPrimaryKeyValue(obj);
    }

    public static String getBeanComment(Field field){
        return BeanAnnotationResolver.getComment(field);
    }
    public static String getBeanComment(Class clazz){
        return BeanAnnotationResolver.getComment(clazz);
    }

    public static Class<? extends Bean2MapFieldConvertor<?,?>> getFieldConvertorBean2Map(Field field){
        ConvertorBean2Map ann=field.getDeclaredAnnotation(ConvertorBean2Map.class);
        ConvertorBean2Map anns= field.getAnnotation(ConvertorBean2Map.class);
        if(ann==null){
            ann=anns;
        }
        if(ann==null){
            return null;
        }
        return ann.value();
    }

    public static boolean isConvertorBean2MapField(Field field){
        return null!=getFieldConvertorBean2Map(field);
    }

    public static<T> Object getConvertBean2MapFieldValue(T obj,Field field){
        Class<? extends Bean2MapFieldConvertor<?,?>> clazz=getFieldConvertorBean2Map(field);
        Object val=ReflectUtil.getVal(obj,field.getName(),true);
        if(clazz==null){
            return val;
        }
        Bean2MapFieldConvertor ins=ReflectUtil.instance(clazz);
        return ins.convert(val);
    }

    public static Class<? extends Map2BeanFieldConvertor<?,?>> getFieldConvertorMap2Bean(Field field){
        ConvertorMap2Bean ann=field.getDeclaredAnnotation(ConvertorMap2Bean.class);
        ConvertorMap2Bean anns= field.getAnnotation(ConvertorMap2Bean.class);
        if(ann==null){
            ann=anns;
        }
        if(ann==null){
            return null;
        }
        return ann.value();
    }

    public static boolean isConvertorMap2BeanField(Field field){
        return null!=getFieldConvertorMap2Bean(field);
    }

    public static<T> T getConvertMap2BeanFieldValue(Object obj,Field field){
        Class<? extends Map2BeanFieldConvertor<?,?>> clazz=getFieldConvertorMap2Bean(field);
        if(clazz==null){
            return (T) obj;
        }
        Map2BeanFieldConvertor ins=ReflectUtil.instance(clazz);
        return (T)ins.convert(obj);
    }

    public static <T> List<T> mapList2BeanList(List<Map<String,Object>> mapList,Class<T> clazz,boolean ignoreCase){
        return BeanResolver.parseMapList(mapList, clazz, ignoreCase);
    }

    public static <T> T map2Bean(Map<String,Object> map,Class<T> clazz,boolean ignoreCase){
        return BeanResolver.parseMap(map, clazz, ignoreCase);
    }
    public static <T> List<T> mapList2BeanList(List<Map<String,Object>> mapList,Class<T> clazz,boolean ignoreCase,boolean ckIgnoreAnnotation)  {
        return BeanResolver.parseMapList(mapList, clazz, ignoreCase,ckIgnoreAnnotation);
    }
    public static <T> T map2Bean(Map<String,Object> map,Class<T> clazz,boolean ignoreCase,boolean ckIgnoreAnnotation)  {
        return BeanResolver.parseMap(map, clazz, ignoreCase,ckIgnoreAnnotation);
    }
    public static <T> List<T> mapList2BeanList(List<Map<String,Object>> mapList,Class<T> clazz,boolean ignoreCase,boolean ckIgnoreAnnotation,boolean useAliasAnnotations)  {
        return BeanResolver.parseMapList(mapList, clazz, ignoreCase,ckIgnoreAnnotation,useAliasAnnotations);
    }

    public static <T> T map2Bean(Map<String,Object> map,Class<T> clazz,boolean ignoreCase,boolean ckIgnoreAnnotation,boolean useAliasAnnotations)  {
        return BeanResolver.parseMap(map, clazz, ignoreCase,ckIgnoreAnnotation,useAliasAnnotations);
    }
}
