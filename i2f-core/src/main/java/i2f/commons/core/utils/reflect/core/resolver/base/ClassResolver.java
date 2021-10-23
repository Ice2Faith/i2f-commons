package i2f.commons.core.utils.reflect.core.resolver.base;


import i2f.commons.core.utils.safe.CheckUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class ClassResolver {
    public static Class getClazz(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {

        }
        return clazz;
    }

    public static <T> Class<T> getClazz(T obj) {
        if (CheckUtil.isNull(obj)) {
            return null;
        }
        Class<T> clazz = (Class<T>) obj.getClass();
        return clazz;
    }
    public static<T> Set<Constructor<T>> getConstructors(Class<T> clazz){
        Constructor<T>[] cons=(Constructor<T>[]) clazz.getConstructors();
        Constructor<T>[] dcons=(Constructor<T>[])clazz.getDeclaredConstructors();
        Set<Constructor<T>> set=new HashSet<>();
        for(Constructor<T> item : cons){
            set.add(item);
        }
        for(Constructor<T> item : dcons){
            set.add(item);
        }
        return set;
    }
    public static<T> T instance(Class<T> clazz,Object ... args){
        Set<Constructor<T>> constructors=getConstructors(clazz);
        if(CheckUtil.isEmptyCollection(constructors)){
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {

            } catch (IllegalAccessException e) {

            }
            return null;
        }
        for(Constructor item : constructors){
            int reqArgsCount=item.getParameterCount();
            if(CheckUtil.notEmptyArray(args)){
                if(reqArgsCount!=args.length){
                    continue;
                }
                boolean isMatched=true;
                Class<?>[] paramTypes=item.getParameterTypes();
                for(int i=0;i< paramTypes.length;i++){
                    if(CheckUtil.isNull(args[i])){
                        continue;
                    }
                    if(!TypeResolver.isSameType(paramTypes[i],args[i].getClass())){
                        isMatched=false;
                        break;
                    }
                }
                if(isMatched){
                    boolean isSuccess=false;
                    try {
                        T ret = (T) item.newInstance(args);
                        return ret;
                    } catch (InstantiationException e) {

                    } catch (IllegalAccessException e) {

                    } catch (InvocationTargetException e) {

                    }
                }
            }else {
                if (reqArgsCount != 0) {
                    continue;
                }
                try {
                    T ret = (T) item.newInstance();
                    return ret;
                } catch (InstantiationException e) {

                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {

                }
            }
        }
        return null;
    }
}
