package i2f.commons.core.utils.reflect.core.resolver;


import i2f.commons.core.utils.data.DataUtil;
import i2f.commons.core.utils.reflect.annotations.FieldColumn;
import i2f.commons.core.utils.reflect.annotations.IgnoreFieldBean2Map;
import i2f.commons.core.utils.reflect.annotations.IgnoreFieldBoth2;
import i2f.commons.core.utils.reflect.annotations.IgnoreFieldMap2Bean;
import i2f.commons.core.utils.reflect.core.resolver.base.AnnotationResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ClassResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.FieldResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ValueResolver;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.str.StringUtil;

import java.lang.reflect.Field;
import java.util.*;

public class BeanResolver {
    public static<T> String[] getBeanFieldsColumnNames(Class<T> clazz,boolean ckIgnoreAnnotation,boolean useAliasAnnotations){
        Vector<String> colNames=new Vector<>();
        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        for(Field item : fields){
            if(ckIgnoreAnnotation && isIgnoreBean2MapField(item)){
                continue;
            }

            String name=item.getName();
            if(useAliasAnnotations){
                name= getBeanFieldColumnName(item);
            }
            colNames.add(name);
        }
        return DataUtil.toArr(colNames,String[].class);
    }

    public static <T> T fieldsNull2Vals(T obj,boolean ignoreCase,String[] fieldNames,Object ... vals){
        if(CheckUtil.isNull(obj)){
            return obj;
        }
        if(CheckUtil.isExEmptyArray(fieldNames,vals)){
            return obj;
        }
        int minLen=fieldNames.length;
        if(minLen>vals.length){
            minLen=vals.length;
        }
        for(int i=0;i<minLen;i+=1){
            Object pval= ValueResolver.getVal(obj,fieldNames[i],ignoreCase);
            if(CheckUtil.isNull(pval)){
                ValueResolver.setVal(obj,fieldNames[i],vals[i],ignoreCase);
            }
        }
        return obj;
    }
    public static <T> List<T> listFields2Value(List<T> list,Object val,boolean ignoreCase,String fieldNames){
        if(CheckUtil.isEmptyCollection(list)){
            return list;
        }
        for(T item : list){
            fields2Value(item,val,ignoreCase,fieldNames);
        }
        return list;
    }

    public static <T> T fields2Null(T obj,boolean ignoreCase,String ... fieldNames){
        return fields2Value(obj,null,ignoreCase,fieldNames);
    }
    public static <T> T fields2Value(T obj,Object val,boolean ignoreCase,String ... fieldNames){
        if(CheckUtil.isNull(obj)){
            return obj;
        }
        if(CheckUtil.isEmptyArray(fieldNames)){
            return obj;
        }

        for(String curName : fieldNames){
            ValueResolver.setVal(obj,curName,val,ignoreCase);
        }
        return obj;
    }

    public static <T> String stringify(T obj,boolean forceAllAttr){
        if(CheckUtil.isNull(obj)){
            return "null";
        }
        Class clazz=obj.getClass();
        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        if(forceAllAttr){
            fields=FieldResolver.getForceAllFields(clazz,true);
        }
        AppendUtil.AppendBuilder attrBuilder= AppendUtil.builder();
        boolean isFirst=true;
        for(Field item : fields){
            if(!isFirst){
                attrBuilder.add(",");
            }
            attrBuilder.adds(item.getName(),"(");
            Object val=ValueResolver.getVal(obj,item.getName(),true);
            attrBuilder.adds(val,":",item.getType().getSimpleName(),")");
            isFirst=false;
        }
        String ret= AppendUtil.str(clazz.getSimpleName(),"{",attrBuilder.done(),"}",":",clazz.getName(),";");
        return ret;
    }
    public static<T,E> void copy(T from,E to,boolean ignoreCase){
        if(CheckUtil.isExNull(from,to)){
            return;
        }
        Class clazzFrom=from.getClass();
        Class clazzTo=to.getClass();

        Set<Field> fieldsFrom=FieldResolver.getAllFields(clazzFrom,true);
        Set<Field> fieldsTo=FieldResolver.getAllFields(clazzTo,true);

        for(Field itemFrom : fieldsFrom){
            String nameFrom= itemFrom.getName();
            for(Field itemTo : fieldsTo){
                String nameTo= itemTo.getName();
                if(ignoreCase){
                    if(!nameFrom.equalsIgnoreCase(nameTo)){
                        continue;
                    }
                }else{
                    if(!nameFrom.equals(nameTo)){
                        continue;
                    }
                }
                Object formVal=ValueResolver.getVal(from,itemFrom.getName(),true);
                ValueResolver.setVal(to,itemTo.getName(),formVal,true);
            }
        }
    }
    public static List<Map<String,Object>> toMapList(List<Object> beans, boolean removeNull){
        return toMapList(beans, removeNull, false);
    }

    public static Map<String,Object> toMap(Object obj,boolean removeNull){
        return toMap(obj, removeNull, false);
    }
    public static Map<String,Object> toMap(Object obj,boolean removeNull,boolean ckIgnoreAnnotation){
        return toMap(obj, removeNull, ckIgnoreAnnotation,false);
    }
    public static List<Map<String,Object>> toMapList(List<Object> beans, boolean removeNull,boolean ckIgnoreAnnotation){
        return toMapList(beans, removeNull, ckIgnoreAnnotation,false);
    }

    public static <T> List<T> parseMapList(List<Map<String,Object>> mapList,Class<T> clazz,boolean ignoreCase){
        return parseMapList(mapList, clazz, ignoreCase,false);
    }

    public static <T> T parseMap(Map<String,Object> map,Class<T> clazz,boolean ignoreCase){
        return parseMap(map,clazz,ignoreCase,false);
    }
    public static <T> List<T> parseMapList(List<Map<String,Object>> mapList,Class<T> clazz,boolean ignoreCase,boolean ckIgnoreAnnotation)  {
        return parseMapList(mapList, clazz, ignoreCase, ckIgnoreAnnotation,false);
    }
    public static <T> T parseMap(Map<String,Object> map,Class<T> clazz,boolean ignoreCase,boolean ckIgnoreAnnotation)  {
        return parseMap(map, clazz, ignoreCase, ckIgnoreAnnotation,false);
    }

    public static List<Map<String,Object>> toMapList(List<Object> beans, boolean removeNull, boolean ckIgnoreAnnotation, boolean useAliasAnnotations){
        List<Map<String,Object>> ret=new ArrayList<>();
        if(CheckUtil.isEmptyCollection(beans)){
            return ret;
        }
        for(Object obj : beans){
            Map<String,Object> map= toMap(obj,removeNull,ckIgnoreAnnotation,useAliasAnnotations);
            ret.add(map);
        }
        return ret;
    }

    public static Map<String,Object> toMap(Object obj, boolean removeNull, boolean ckIgnoreAnnotation, boolean useAliasAnnotations){
        Map<String,Object> ret=new HashMap<>();

        if(CheckUtil.isNull(obj)){
            return ret;
        }

        Class clazz=obj.getClass();
        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        for(Field item : fields){
            if(ckIgnoreAnnotation && isIgnoreBean2MapField(item)){
                continue;
            }

            String name=item.getName();
            Object val= ValueResolver.getVal(obj,name,true);
            if(CheckUtil.isNull(val) && removeNull){
                continue;
            }
            if(useAliasAnnotations){
                name=getBeanFieldColumnName(item);
            }
            ret.put(name, val);
        }

        return ret;
    }

    public static <T> List<T> parseMapList(List<Map<String,Object>> mapList, Class<T> clazz, boolean ignoreCase, boolean ckIgnoreAnnotation, boolean useAliasAnnotations)  {
        List<T> ret=new ArrayList<>();
        if(CheckUtil.isEmptyCollection(mapList)){
            return ret;
        }
        for(Map<String,Object> item : mapList){
            T bean= parseMap(item,clazz,ignoreCase,ckIgnoreAnnotation,useAliasAnnotations);
            ret.add(bean);
        }
        return ret;
    }

    public static <T> T parseMap(Map<String,Object> map, Class<T> clazz, boolean ignoreCase, boolean ckIgnoreAnnotation, boolean useAliasAnnotations)  {
        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        T ret= ClassResolver.instance(clazz);
        for(Field field :fields){
            if(ckIgnoreAnnotation && isIgnoreMap2BeanField(field)){
                continue;
            }
            String name= field.getName();
            if(useAliasAnnotations){
                name=getBeanFieldColumnName(field);
            }
            for(String key : map.keySet()){
                if(ignoreCase){
                    if(!name.equalsIgnoreCase(key)){
                        continue;
                    }
                }else{
                    if(!name.equals(key)){
                        continue;
                    }
                }
                ValueResolver.setVal(ret,field.getName(),map.get(key),true);
            }
        }
        return ret;
    }

    public static boolean isIgnoreBean2MapField(Field item){
        IgnoreFieldBean2Map ann= AnnotationResolver.getFieldAnnotation(item,
                false,false,
                true,IgnoreFieldBean2Map.class);
        if(ann==null){
            return isIgnoreBothField(item);
        }
        return ann.value();
    }

    public static boolean isIgnoreMap2BeanField(Field item){
        IgnoreFieldMap2Bean ann=AnnotationResolver.getFieldAnnotation(item,
                false,false,
                true,IgnoreFieldMap2Bean.class);
        if(ann==null){
            return isIgnoreBothField(item);
        }
        return ann.value();
    }

    public static boolean isIgnoreBothField(Field item){
        IgnoreFieldBoth2 ann=AnnotationResolver.getFieldAnnotation(item,
                false,false,
                true,IgnoreFieldBoth2.class);
        if(ann==null){
            return false;
        }
        return ann.value();
    }

    public static String getBeanFieldColumnName(Field field){
        FieldColumn ann=AnnotationResolver.getFieldAnnotation(field,
                false,false,
                true,FieldColumn.class);
        String fieldName=field.getName();
        if(ann==null){
            return fieldName;
        }
        String annName=ann.value();
        if(CheckUtil.notEmptyStr(annName,true)){
            fieldName=annName;
        }
        if(ann.underScore()){
            fieldName= StringUtil.toUnderScore(fieldName);
        }
        return fieldName;
    }
}
