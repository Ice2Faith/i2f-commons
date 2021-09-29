package i2f.commons.core.utils.reflect;


import i2f.commons.core.utils.reflect.annotations.FieldTags;
import i2f.commons.core.utils.reflect.core.resolver.base.AnnotationResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.FieldResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ValueResolver;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;

import java.lang.reflect.Field;
import java.util.*;

public class BeanTagsUtil {
    public static<T,E> E copyFields(T from,E to,String ... tags){
        if(CheckUtil.isExNull(from,to)){
            return to;
        }
        Class fromClazz=from.getClass();
        Class toClazz=to.getClass();
        Field[] fields=getTagsFieldsProxy(true,fromClazz,tags);
        for(Field item : fields){
            String name= item.getName();
            Object val= ValueResolver.getVal(from,fromClazz,name,false);
            ValueResolver.setVal(to,toClazz,name,val,false);
        }
        return to;
    }

    public static <T> List<T> listFields2Null(List<T> list, String ... tags){
        return listFields2Value(list,null,tags);
    }
    public static <T> List<T> listFields2Value(List<T> list, Object val, String ... tags){
        if(CheckUtil.isEmptyCollection(list)){
            return list;
        }
        for(T item : list){
            fields2Value(item,val,tags);
        }
        return list;
    }
    public static <T> T fields2Null(T obj, String ... tags){
        return fields2Value(obj,null,tags);
    }
    public static <T> T fields2Value(T obj, Object val, String ... tags){
        if(CheckUtil.isNull(obj)){
            return obj;
        }
        if(CheckUtil.isEmptyArray(tags)){
            return obj;
        }

        Class clazz=obj.getClass();
        Field[] fields=getIncludeTagsFields(clazz,tags);
        for(Field item : fields){
            ValueResolver.setVal(obj,item.getName(),val,false);
        }

        return obj;
    }

    public static<T> String[] getFieldsNames(Class<T> clazz, String prefix, String ... tags){
        return getFieldsNamesProxy(true,clazz,prefix,tags);
    }
    public static<T> Object[] getFieldsValues(T obj, String ... tags){
        return getFieldsValuesProxy(true,obj,tags);
    }

    public static<T> Map<String,Object> getFieldsValuesMap(boolean trimNull,T obj,String ... tags){
        return getFieldsValuesMapProxy(true,trimNull,obj,tags);
    }
    public static<T> Map<String,Object> getExcludeFieldsValuesMap(boolean trimNull,T obj,String ... tags){
        return getFieldsValuesMapProxy(false,trimNull,obj,tags);
    }

    private static<T> Map<String,Object> getFieldsValuesMapProxy(boolean isInclude,boolean trimNull,T obj,String ... tags){
        Map<String,Object> ret=new HashMap<>();
        if(obj==null){
            return ret;
        }
        Class clazz=obj.getClass();
        Field[] fields=getTagsFieldsProxy(isInclude,clazz,tags);
        for (int i = 0; i < fields.length; i++) {
            Object val=ValueResolver.getVal(obj,clazz,fields[i].getName(),false);
            if(trimNull && val==null){
                continue;
            }
            ret.put(fields[i].getName(),val);
        }
        return ret;
    }

    public static<T> String[] getExcludeFieldsNames(Class<T> clazz, String prefix, String ... tags){
        return getFieldsNamesProxy(false,clazz,prefix,tags);
    }
    public static<T> Object[] getExcludeFieldsValues(T obj, String ... tags){
        return getFieldsValuesProxy(false,obj,tags);
    }

    private static<T> String[] getFieldsNamesProxy(boolean isInclude,Class<T> clazz, String prefix, String ... tags){
        Field[] fields=getTagsFieldsProxy(isInclude,clazz,tags);
        String[] ret=new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            if(prefix!=null) {
                ret[i] = AppendUtil.str(prefix, fields[i].getName());
            }else{
                ret[i]=fields[i].getName();
            }
        }
        return ret;
    }
    private static<T> Object[] getFieldsValuesProxy(boolean isInclude,T obj, String ... tags){
        if(obj==null){
            return new Object[0];
        }
        Class clazz=obj.getClass();
        Field[] fields=getTagsFieldsProxy(isInclude,clazz,tags);
        Object[] ret=new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            ret[i]=ValueResolver.getVal(obj,clazz,fields[i].getName(),false);
        }
        return ret;
    }

    public static Field[] getIncludeTagsFields(Class clazz, String ... tags){
        return getTagsFieldsProxy(true,clazz,tags);
    }
    public static Field[] getExcludeTagsFields(Class clazz, String ... tags){
        return getTagsFieldsProxy(false,clazz,tags);
    }
    private static Field[] getTagsFieldsProxy(boolean isInclude,Class clazz, String ... tags){
        Vector<Field> ret=new Vector<>();
        if(CheckUtil.isEmptyArray(tags)){
            return new Field[]{};
        }
        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        for(Field item : fields){
            if(isInclude==isIncludeTagField(item,tags)){
                ret.add(item);
            }
        }
        if(CheckUtil.isEmptyCollection(ret)){
            return new Field[]{};
        }
        Field[] retArr=new Field[ret.size()];
        int count=0;
        for(Field item : ret){
            retArr[count]=item;
            count++;
        }
        return retArr;
    }
    public static boolean isIncludeTagField(Field field,String[] reqTags){
        FieldTags ann= AnnotationResolver.getFieldAnnotation(field,
                false,false,
                true,FieldTags.class);
        if(ann==null){
            return false;
        }
        String[] tags=ann.value();
        for(String tag : tags){
            for(String reqTag : reqTags){
                if(tag.equals(reqTag)){
                    return true;
                }
            }
        }
        return false;
    }
}
