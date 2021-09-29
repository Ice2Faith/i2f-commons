package i2f.commons.core.utils.reflect.core.resolver;


import i2f.commons.core.utils.reflect.annotations.BeanComment;
import i2f.commons.core.utils.reflect.annotations.BeanTable;
import i2f.commons.core.utils.reflect.annotations.FieldForeignKeyFor;
import i2f.commons.core.utils.reflect.annotations.FieldPrimaryKey;
import i2f.commons.core.utils.reflect.core.resolver.base.AnnotationResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.FieldResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ValueResolver;
import i2f.commons.core.utils.safe.CheckUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanAnnotationResolver {
    public static String getTableName(Class clazz){
        BeanTable ann= AnnotationResolver.getClassAnnotation(clazz,
                false,false,
                BeanTable.class);
        String clazzName=clazz.getSimpleName();
        if(ann==null){
            return clazzName;
        }
        String annName=ann.value();
        if(CheckUtil.notEmptyStr(annName,true)){
            return annName;
        }else{
            return clazzName;
        }
    }

    public static String getComment(Field field){
        BeanComment ann=AnnotationResolver.getFieldAnnotation(field,
                false,false,
                true,BeanComment.class);
        if(ann==null){
            return "";
        }
        return ann.value();
    }
    public static String getComment(Class clazz){
        BeanComment ann= AnnotationResolver.getClassAnnotation(clazz,
                false,false,
                BeanComment.class);
        if(ann==null){
            return "";
        }
        return ann.value();
    }

    public static Set<Field> getForeignKeyForFields(Class clazz){
        Set<Field> ret=new HashSet<Field>();
        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        for(Field item : fields) {
            FieldForeignKeyFor ann = AnnotationResolver.getFieldAnnotation(item,
                    false, false,
                    true, FieldForeignKeyFor.class);
            if (ann == null) {
                continue;
            }
            ret.add(item);
        }
        return ret;
    }
    public static <T> Map<Field,Object> getForeignKeyFieldAndForVal(T obj) {
        if (CheckUtil.isNull(obj)) {
            return null;
        }
        Map<Field, Object> ret = new HashMap<>();
        Class clazz = obj.getClass();
        Set<Field> fields = getForeignKeyForFields(clazz);
        for (Field item : fields) {
            FieldForeignKeyFor ann = AnnotationResolver.getFieldAnnotation(item,
                    false, false,
                    true, FieldForeignKeyFor.class);
            if (ann == null) {
                continue;
            }
            String forFieldName = ann.value();
            if (CheckUtil.isEmptyStr(forFieldName, true)) {
                continue;
            }
            Object val = ValueResolver.getVal(obj, forFieldName, true);
            ret.put(item, val);
        }
        return ret;
    }
    public static boolean isBeanPrimaryKeyField(Field field){
        FieldPrimaryKey ann=AnnotationResolver.getFieldAnnotation(field,
                false,false,
                true,FieldPrimaryKey.class);
        if(ann==null){
            return false;
        }
        return ann.value();
    }
    public static boolean isBeanAutoPrimaryKey(Field field){
        FieldPrimaryKey ann=AnnotationResolver.getFieldAnnotation(field,
                false,false,
                true,FieldPrimaryKey.class);
        if(ann==null){
            return false;
        }
        return ann.isAuto();
    }

    public static Field getBeanPrimaryKeyField(Class clazz){
        Set<Field> allFields=FieldResolver.getAllFields(clazz,true);
        for(Field item : allFields){
            if(isBeanPrimaryKeyField(item)){
                return item;
            }
        }
        return null;
    }

    public static boolean containsBeanPrimaryKey(Class clazz){
        return null!=getBeanPrimaryKeyField(clazz);
    }



    public static String getBeanPrimaryKeyColumnName(Class clazz){
        Field field=getBeanPrimaryKeyField(clazz);
        if(field==null){
            return null;
        }
        return BeanResolver.getBeanFieldColumnName(field);
    }
    public static <T> String getBeanPrimaryKeyColumnName(T obj){
        if(CheckUtil.isNull(obj)){
            return null;
        }
        Class clazz=obj.getClass();
        return getBeanPrimaryKeyColumnName(clazz);
    }
    public static<T,E> E getBeanPrimaryKeyValue(T obj){
        if(CheckUtil.isNull(obj)){
            return null;
        }
        Class clazz=obj.getClass();
        Field field=getBeanPrimaryKeyField(clazz);
        if(field==null){
            return null;
        }
        E val=ValueResolver.getVal(obj,field.getName(),true);
        return val;
    }
}
