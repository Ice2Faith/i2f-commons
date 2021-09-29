package i2f.commons.core.utils.reflect.core.resolver.base;


import i2f.commons.core.utils.safe.CheckUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldResolver {
    public static Set<Field> getAllFields(Class clazz,boolean trimStaticFinal) {
        Field[] allFields = clazz.getFields();
        Field[] fields = clazz.getDeclaredFields();
        Set<Field> fieldSet = new HashSet<>();
        for (Field item : allFields) {
            if(trimStaticFinal && isStaticFinal(item)){
                continue;
            }
            fieldSet.add(item);
        }
        for (Field item : fields) {
            if(trimStaticFinal && isStaticFinal(item)){
                continue;
            }
            fieldSet.add(item);
        }

        return fieldSet;
    }
    public static Set<Field> getForceAllFields(Class clazz,boolean trimStaticFinal){
        Set<Field> fieldSet = new HashSet<>();
        if(clazz==null || Object.class.equals(clazz)){
            return fieldSet;
        }
        Set<Field> curFields=getAllFields(clazz,trimStaticFinal);
        fieldSet.addAll(curFields);

        Class superClass=clazz.getSuperclass();
        Set<Field> superFields=getForceAllFields(superClass,trimStaticFinal);
        fieldSet.addAll(superFields);

        return fieldSet;
    }
    public static Set<Field> getAllFieldsWithAnnotations(Class clazz, boolean trimStaticFinal, boolean forceAll, Class<? extends Annotation> ... annotationClasses){
        Set<Field> fields=new HashSet<>();
        if(forceAll){
            fields=getForceAllFields(clazz,trimStaticFinal);
        }else{
            fields=getAllFields(clazz, trimStaticFinal);
        }
        Set<Field> ret=new HashSet<>();
        for(Field item : fields){
            for(Class annotationClass : annotationClasses){
                Annotation ann=AnnotationResolver.getFieldAnnotation(item,false,false,true,annotationClass);
                if(ann!=null){
                    ret.add(item);
                    continue;
                }
            }
        }
        return ret;
    }

    public static List<Field> getFields(Class clazz, boolean ignoreCase, String... fieldNames) {
        List<Field> ret = new ArrayList<>();
        if (CheckUtil.isNull(clazz) || CheckUtil.isEmptyArray(fieldNames)) {
            return ret;
        }
        Set<Field> allFields = getAllFields(clazz,true);
        for (String item : fieldNames) {
            for (Field field : allFields) {
                if (ignoreCase) {
                    if (field.getName().equalsIgnoreCase(item)) {
                        ret.add(field);
                    }
                } else {
                    if (field.getName().equals(item)) {
                        ret.add(field);
                    }
                }
            }
        }

        return ret;
    }
    public static boolean isStaticFinal(Field field){
        int mod=field.getModifiers();
        return isStaticFinal(mod);
    }
    public static boolean isStaticFinal(int modifier){
        if(Modifier.isFinal(modifier) && Modifier.isStatic(modifier)){
            return true;
        }
        return false;
    }

}
