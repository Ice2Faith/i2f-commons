package i2f.commons.core.utils.reflect.core.resolver.base;


import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.str.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class MethodResolver {
    public static Method fetchMethod(Collection<Method> methods,Object ... args){
        if(methods.size()==1){
            return methods.iterator().next();
        }else if(methods.size()>1){
            Iterator<Method> it= methods.iterator();
            while (it.hasNext()){
                Method method=it.next();
                if(args.length!=method.getParameterCount()){
                    continue;
                }
                Class[] types= method.getParameterTypes();
                boolean matched=true;
                for (int i = 0; i < types.length; i++) {
                    if(args[i]==null){
                        continue;
                    }
                    if(!TypeResolver.isSameType(types[i],args[i].getClass())){
                        matched=false;
                        break;
                    }
                }
                if(matched){
                    return method;
                }
            }

        }
        return null;
    }
    public static Method getTargetMethod(Class clazz,String methodName,Object ... args) throws Exception {
        List<Method> methods=getMethods(clazz,false,methodName);
        return fetchMethod(methods,args);
    }

    public static Object invoke(Class clazz,Object obj,String methodName,Object ... args) throws Exception {
        try{
            Method method=getTargetMethod(clazz,methodName,args);
            Object rs=method.invoke(obj,args);
            return rs;
        }catch(Exception e){
            throw new Exception("invoke method error."+e.getMessage(),e);
        }
    }

    public static Object invokeStaticMethod(Class clazz,String methodName,Object ... args) throws Exception {
        return invoke(clazz,null,methodName,args);
    }

    public static Object invokeMethod(Object obj,String methodName,Object ... args) throws Exception {
        return invoke(obj.getClass(),obj,methodName,args);
    }

    public static Method matchMethod(Class clazz, String methodName, Class<?>... paramTypes) {
        if (CheckUtil.isNull(clazz) || CheckUtil.isEmptyStr(methodName, false)) {
            return null;
        }
        Method ret = null;
        Set<Method> methods = getAllMethods(clazz);
        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            boolean matched = true;
            Class<?>[] types = method.getParameterTypes();
            if (types.length != paramTypes.length) {
                continue;
            }
            for (int i = 0; i < types.length; i++) {
                if (!TypeResolver.isSameType(types[i], paramTypes[i])) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                ret = method;
                break;
            }
        }
        return ret;
    }
    public static Set<Method> getAllMethods(Class clazz) {
        Method[] allMethods = clazz.getMethods();
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> methodSet = new HashSet<>();
        for (Method item : allMethods) {
            methodSet.add(item);
        }
        for (Method item : methods) {
            methodSet.add(item);
        }

        return methodSet;
    }
    public static List<Method> getMethods(Class clazz, boolean ignoreCase, String... methodNames) {
        List<Method> ret = new ArrayList<>();
        if (CheckUtil.isNull(clazz) || CheckUtil.isEmptyArray(methodNames)) {
            return ret;
        }
        Set<Method> allMethods = getAllMethods(clazz);
        for (String item : methodNames) {
            for (Method method : allMethods) {
                if (ignoreCase) {
                    if (method.getName().equalsIgnoreCase(item)) {
                        ret.add(method);
                    }
                } else {
                    if (method.getName().equals(item)) {
                        ret.add(method);
                    }
                }
            }
        }

        return ret;
    }
    public static Set<Method> getAllMethodsWithAnnotations(Class clazz, Class<? extends Annotation> ... annotationClasses){
        Set<Method> fields=getAllMethods(clazz);
        Set<Method> ret=new HashSet<>();
        for(Method item : fields){
            for(Class annotationClass : annotationClasses){
                Annotation ann=AnnotationResolver.getMethodAnnotation(item,false,false,true,annotationClass);
                if(ann!=null){
                    ret.add(item);
                    continue;
                }
            }
        }
        return ret;
    }
    public static String getter(String fieldName) {
        return AppendUtil.builder(null,"get", StringUtil.firstUpperCase(fieldName)).done();
    }
    public static String setter(String fieldName) {
        return AppendUtil.builder(null,"set", StringUtil.firstUpperCase(fieldName)).done();
    }
    public static Method findGetter(Class clazz, String fieldName) {
        Method method = MethodResolver.matchMethod(clazz, getter(fieldName));
        return method;
    }
    public static Method findSetter(Class clazz, String fieldName, Class<?> paramType) {
        Method method = MethodResolver.matchMethod(clazz, setter(fieldName), paramType);
        return method;
    }
    public static Method findGetter(Class clazz, Field field) {
        return findGetter(clazz, field.getName());
    }
    public static Method findSetter(Class clazz, Field field) {
        return findSetter(clazz, field.getName(), field.getType());
    }

}
