package i2f.commons.core.utils.reflect.simple.reflect.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/14 11:03
 * @desc
 */
public class FastReflect {
    protected static volatile ConcurrentHashMap<Class, Set<Field>> cacheFields=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Set<Method>> cacheMethods=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Set<Method>> cacheGetters=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Set<Method>> cacheSetters=new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Class, Map<String,Set<Method>>> fastGetter=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Map<String,Set<Method>>> fastSetter=new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,Map<String,Field>> fastField=new ConcurrentHashMap<>();

    protected static boolean containsFastGetterProxy(Class clazz, String fieldName){
        if(fastGetter.containsKey(clazz)){
            Map<String,Set<Method>> map= fastGetter.get(clazz);
            if(map.containsKey(fieldName)){
                return true;
            }
        }
        return false;
    }

    protected static Set<Method> getFastGetterProxy(Class clazz, String fieldName){
        if(fastGetter.containsKey(clazz)){
            Map<String,Set<Method>> map= fastGetter.get(clazz);
            if(map.containsKey(fieldName)){
                return map.get(fieldName);
            }
        }
        return new HashSet<>();
    }

    protected static void setFastGetterProxy(Class clazz, String fieldName, Set<Method> methods){
        if(!fastGetter.containsKey(clazz)){
            fastGetter.put(clazz,new HashMap<>());
        }
        Map<String,Set<Method>> map= fastGetter.get(clazz);
        map.put(fieldName,methods);
    }

    protected static boolean containsFastSetterProxy(Class clazz, String fieldName){
        if(fastSetter.containsKey(clazz)){
            Map<String,Set<Method>> map= fastSetter.get(clazz);
            if(map.containsKey(fieldName)){
                return true;
            }
        }
        return false;
    }

    protected static Set<Method> getFastSetterProxy(Class clazz, String fieldName){
        if(fastSetter.containsKey(clazz)){
            Map<String,Set<Method>> map= fastSetter.get(clazz);
            if(map.containsKey(fieldName)){
                return map.get(fieldName);
            }
        }
        return new HashSet<>();
    }

    protected static void setFastSetterProxy(Class clazz, String fieldName, Set<Method> methods){
        if(!fastSetter.containsKey(clazz)){
            fastSetter.put(clazz,new HashMap<>());
        }
        Map<String,Set<Method>> map= fastSetter.get(clazz);
        map.put(fieldName,methods);
    }

    protected static boolean containsFastFieldProxy(Class clazz, String fieldName){
        if(fastField.containsKey(clazz)){
            Map<String,Field> map= fastField.get(clazz);
            if(map.containsKey(fieldName)){
                return true;
            }
        }
        return false;
    }

    protected static Field getFastFieldProxy(Class clazz, String fieldName){
        if(fastField.containsKey(clazz)){
            Map<String,Field> map= fastField.get(clazz);
            if(map.containsKey(fieldName)){
                return map.get(fieldName);
            }
        }
        return null;
    }

    protected static void setFastFieldProxy(Class clazz, String fieldName, Field field){
        if(!fastField.containsKey(clazz)){
            fastField.put(clazz,new HashMap<>());
        }
        Map<String,Field> map= fastField.get(clazz);
        map.put(fieldName,field);
    }

    public static Set<Method> getAllMethods(Class clazz){
        if(cacheMethods.containsKey(clazz)){
            return cacheMethods.get(clazz);
        }
        Set<Method> ret=new HashSet<>();
        for(Method item : clazz.getMethods()){
            ret.add(item);
        }
        for(Method item : clazz.getDeclaredMethods()){
            ret.add(item);
        }
        cacheMethods.put(clazz,ret);
        return ret;
    }

    public static Set<Method> getAllGetters(Class clazz){
        if(cacheGetters.containsKey(clazz)){
            return cacheGetters.get(clazz);
        }
        Set<Method> ret=new HashSet<>();
        Set<Method> methods=getAllMethods(clazz);
        for(Method item : methods){
            if(methodIsGetter(item)){
                ret.add(item);
            }
        }
        cacheGetters.put(clazz,ret);
        return ret;
    }

    public static Set<Method> getAllSetters(Class clazz){
        if(cacheSetters.containsKey(clazz)){
            return cacheSetters.get(clazz);
        }
        Set<Method> ret=new HashSet<>();
        Set<Method> methods=getAllMethods(clazz);
        for(Method item : methods){
            if(methodIsSetter(item)){
                ret.add(item);
            }
        }
        cacheSetters.put(clazz,ret);
        return ret;
    }

    public static Set<Field> getAllFields(Class clazz){
        if(cacheFields.containsKey(clazz)){
            return cacheFields.get(clazz);
        }
        Set<Field> ret=new HashSet<>();
        for(Field item : clazz.getFields()){
            ret.add(item);
        }
        for(Field item : clazz.getDeclaredFields()){
            ret.add(item);
        }
        cacheFields.put(clazz,ret);
        return ret;
    }

    public static Set<Method> findGetters(Class clazz,String fieldName){
        if(containsFastGetterProxy(clazz,fieldName)){
            return getFastGetterProxy(clazz,fieldName);
        }
        Set<Method> ret=new HashSet<>();
        String methodName=fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
        for(Method item : getAllGetters(clazz)){
            String name=item.getName();
            if(name.equals("get"+methodName) || name.equals("is"+methodName)){
                ret.add(item);
            }
        }
        setFastGetterProxy(clazz,fieldName,ret);
        return ret;
    }

    public static Set<Method> findSetters(Class clazz,String fieldName){
        if(containsFastSetterProxy(clazz,fieldName)){
            return getFastSetterProxy(clazz,fieldName);
        }
        Set<Method> ret=new HashSet<>();
        String methodName=fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
        for(Method item : getAllSetters(clazz)){
            String name=item.getName();
            if(name.equals("set"+methodName)){
                ret.add(item);
            }
        }
        setFastSetterProxy(clazz,fieldName,ret);
        return ret;
    }

    public static Field findField(Class clazz,String fieldName){
        if(containsFastFieldProxy(clazz,fieldName)){
            return getFastFieldProxy(clazz,fieldName);
        }
        Field field=null;
        Set<Field> fields=getAllFields(clazz);
        for(Field item : fields) {
            String name = item.getName();
            if (name.equals(fieldName)) {
                field=item;
                break;
            }
        }
        setFastFieldProxy(clazz,fieldName,field);
        return field;
    }

    /**
     * 判断方法是否是一个标准的getter
     * @param method
     * @return
     */
    public static boolean methodIsGetter(Method method){
        if(method.getParameterCount()!=0){
            return false;
        }
        if(method.getReturnType().equals(Void.class)){
            return false;
        }
        if(!method.getName().startsWith("get") && !method.getName().startsWith("is")){
            return false;
        }
        return true;
    }

    /**
     * 判断方法是否是一个标准的setter
     * @param method
     * @return
     */
    public static boolean methodIsSetter(Method method){
        if(method.getParameterCount()!=1){
            return false;
        }
        if(!method.getName().startsWith("set")){
            return false;
        }
        return true;
    }

}
