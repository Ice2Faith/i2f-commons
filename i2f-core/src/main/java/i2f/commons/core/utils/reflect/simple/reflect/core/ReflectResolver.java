package i2f.commons.core.utils.reflect.simple.reflect.core;

import i2f.commons.core.utils.reflect.simple.reflect.PropertyAccessor;
import i2f.commons.core.utils.reflect.simple.reflect.impl.FieldValueAccessor;
import i2f.commons.core.utils.reflect.simple.reflect.impl.MethodValueAccessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/14 11:03
 * @desc 反射核心处理类
 * 设计原则
 * 优先使用缓存查找方式，一定程度上降低反射带来的效率问题
 */
public class ReflectResolver {
    protected static volatile ConcurrentHashMap<Class, Set<Field>> cacheFields = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Set<Method>> cacheMethods = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Set<Method>> cacheGetters = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Set<Method>> cacheSetters = new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Class, Map<String, Set<Method>>> fastGetter = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Map<String, Set<Method>>> fastSetter = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Map<String, Field>> fastField = new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Class, List<PropertyAccessor>> logicalReadableFields = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, List<PropertyAccessor>> logicalWritableFields = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,List<PropertyAccessor>> logicalReadWriteFields=new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Class, Set<Field>> cacheForceFields = new ConcurrentHashMap<>();

    /**
     * 获取指定类的逻辑上可读的所有属性
     * 认为本类字段以及所有getter包含的都是逻辑上的可读字段
     *
     * @param clazz
     * @return
     */
    public static List<PropertyAccessor> getLogicalReadableFields(Class clazz) {
        if (logicalReadableFields.containsKey(clazz)) {
            return logicalReadableFields.get(clazz);
        }
        List<PropertyAccessor> ret = new ArrayList<>();
        Set<String> fields = new HashSet<>();
        Set<Method> methods = getAllGetters(clazz);
        for (Method item : methods) {
            String name = item.getName();
            if (name.startsWith("get")) {
                name = name.substring("get".length());
            } else if (name.startsWith("is")) {
                name = name.substring("is".length());
            }
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            if (fields.contains(name)) {
                continue;
            }
            fields.add(name);
            MethodValueAccessor accessor = new MethodValueAccessor(item, null, name, item.getReturnType());
            ret.add(accessor);
        }

        Set<Field> fieldSet = getAllFields(clazz);
        for (Field item : fieldSet) {
            String name = item.getName();
            if (fields.contains(name)) {
                continue;
            }
            fields.add(name);
            FieldValueAccessor accessor = new FieldValueAccessor(item);
            ret.add(accessor);
        }

        logicalReadableFields.put(clazz, ret);
        return ret;
    }

    /**
     * 获取指定类的逻辑上可写的所有属性
     * 认为本类字段以及所有setter包含的都是逻辑上可写的字段
     *
     * @param clazz
     * @return
     */
    public static List<PropertyAccessor> getLogicalWritableFields(Class clazz) {
        if (logicalWritableFields.containsKey(clazz)) {
            return logicalWritableFields.get(clazz);
        }
        List<PropertyAccessor> ret = new ArrayList<>();
        Set<String> fields = new HashSet<>();
        Set<Method> methods = getAllSetters(clazz);
        for (Method item : methods) {
            String name = item.getName();
            if (name.startsWith("set")) {
                name = name.substring("set".length());
            }
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            if (fields.contains(name)) {
                continue;
            }
            fields.add(name);
            MethodValueAccessor accessor = new MethodValueAccessor(null, item, name, item.getParameterTypes()[0]);
            ret.add(accessor);
        }

        Set<Field> fieldSet = getAllFields(clazz);
        for (Field item : fieldSet) {
            String name = item.getName();
            if (fields.contains(name)) {
                continue;
            }
            fields.add(name);
            FieldValueAccessor accessor = new FieldValueAccessor(item);
            ret.add(accessor);
        }

        logicalWritableFields.put(clazz, ret);
        return ret;
    }

    public static List<PropertyAccessor> getLogicalReadWriteFields(Class clazz){
        if(logicalReadWriteFields.containsKey(clazz)){
            return logicalReadWriteFields.get(clazz);
        }
        List<PropertyAccessor> ret=new ArrayList<>();
        Set<String> set=new HashSet<>();
        List<PropertyAccessor> writers=getLogicalWritableFields(clazz);
        List<PropertyAccessor> readers=getLogicalReadableFields(clazz);
        for(PropertyAccessor witem : writers){
            String wname=witem.getName();
            if(witem instanceof FieldValueAccessor){
                if(!set.contains(wname)){
                    set.add(wname);
                    ret.add(witem);
                    continue;
                }
            }

            for(PropertyAccessor ritem : readers){
                String rname= ritem.getName();
                if(ritem instanceof FieldValueAccessor){
                    continue;
                }
                if(rname.equals(wname)){
                    if(set.contains(rname)){
                        continue;
                    }
                    MethodValueAccessor racc=(MethodValueAccessor)ritem;
                    MethodValueAccessor wacc=(MethodValueAccessor)witem;
                    PropertyAccessor accessor=new MethodValueAccessor(racc.getGetter(),wacc.getSetter(),rname,ritem.getType());
                    ret.add(accessor);
                    set.add(rname);
                }
            }
        }

        logicalReadWriteFields.put(clazz,ret);
        return ret;
    }

    protected static boolean containsFastGetterProxy(Class clazz, String fieldName) {
        if (fastGetter.containsKey(clazz)) {
            Map<String, Set<Method>> map = fastGetter.get(clazz);
            if (map.containsKey(fieldName)) {
                return true;
            }
        }
        return false;
    }

    protected static Set<Method> getFastGetterProxy(Class clazz, String fieldName) {
        if (fastGetter.containsKey(clazz)) {
            Map<String, Set<Method>> map = fastGetter.get(clazz);
            if (map.containsKey(fieldName)) {
                return map.get(fieldName);
            }
        }
        return new HashSet<>();
    }

    protected static void setFastGetterProxy(Class clazz, String fieldName, Set<Method> methods) {
        if (!fastGetter.containsKey(clazz)) {
            fastGetter.put(clazz, new HashMap<>());
        }
        Map<String, Set<Method>> map = fastGetter.get(clazz);
        map.put(fieldName, methods);
    }

    protected static boolean containsFastSetterProxy(Class clazz, String fieldName) {
        if (fastSetter.containsKey(clazz)) {
            Map<String, Set<Method>> map = fastSetter.get(clazz);
            if (map.containsKey(fieldName)) {
                return true;
            }
        }
        return false;
    }

    protected static Set<Method> getFastSetterProxy(Class clazz, String fieldName) {
        if (fastSetter.containsKey(clazz)) {
            Map<String, Set<Method>> map = fastSetter.get(clazz);
            if (map.containsKey(fieldName)) {
                return map.get(fieldName);
            }
        }
        return new HashSet<>();
    }

    protected static void setFastSetterProxy(Class clazz, String fieldName, Set<Method> methods) {
        if (!fastSetter.containsKey(clazz)) {
            fastSetter.put(clazz, new HashMap<>());
        }
        Map<String, Set<Method>> map = fastSetter.get(clazz);
        map.put(fieldName, methods);
    }

    protected static boolean containsFastFieldProxy(Class clazz, String fieldName) {
        if (fastField.containsKey(clazz)) {
            Map<String, Field> map = fastField.get(clazz);
            if (map.containsKey(fieldName)) {
                return true;
            }
        }
        return false;
    }

    protected static Field getFastFieldProxy(Class clazz, String fieldName) {
        if (fastField.containsKey(clazz)) {
            Map<String, Field> map = fastField.get(clazz);
            if (map.containsKey(fieldName)) {
                return map.get(fieldName);
            }
        }
        return null;
    }

    protected static void setFastFieldProxy(Class clazz, String fieldName, Field field) {
        if (!fastField.containsKey(clazz)) {
            fastField.put(clazz, new HashMap<>());
        }
        Map<String, Field> map = fastField.get(clazz);
        map.put(fieldName, field);
    }

    /**
     * 获取指定类的所有方法
     *
     * @param clazz
     * @return
     */
    public static Set<Method> getAllMethods(Class clazz) {
        if (cacheMethods.containsKey(clazz)) {
            return cacheMethods.get(clazz);
        }
        Set<Method> ret = new HashSet<>();
        for (Method item : clazz.getMethods()) {
            ret.add(item);
        }
        for (Method item : clazz.getDeclaredMethods()) {
            ret.add(item);
        }
        cacheMethods.put(clazz, ret);
        return ret;
    }

    /**
     * 获取指定类的所有getter
     *
     * @param clazz
     * @return
     */
    public static Set<Method> getAllGetters(Class clazz) {
        if (cacheGetters.containsKey(clazz)) {
            return cacheGetters.get(clazz);
        }
        Set<Method> ret = new HashSet<>();
        Set<Method> methods = getAllMethods(clazz);
        for (Method item : methods) {
            if (methodIsGetter(item)) {
                ret.add(item);
            }
        }
        cacheGetters.put(clazz, ret);
        return ret;
    }

    /**
     * 获取指定类的所有setter
     *
     * @param clazz
     * @return
     */
    public static Set<Method> getAllSetters(Class clazz) {
        if (cacheSetters.containsKey(clazz)) {
            return cacheSetters.get(clazz);
        }
        Set<Method> ret = new HashSet<>();
        Set<Method> methods = getAllMethods(clazz);
        for (Method item : methods) {
            if (methodIsSetter(item)) {
                ret.add(item);
            }
        }
        cacheSetters.put(clazz, ret);
        return ret;
    }

    /**
     * 获取指定类的所有字段，仅本类
     *
     * @param clazz
     * @return
     */
    public static Set<Field> getAllFields(Class clazz) {
        if (cacheFields.containsKey(clazz)) {
            return cacheFields.get(clazz);
        }
        Set<Field> ret = new HashSet<>();
        for (Field item : clazz.getFields()) {
            ret.add(item);
        }
        for (Field item : clazz.getDeclaredFields()) {
            ret.add(item);
        }
        cacheFields.put(clazz, ret);
        return ret;
    }

    /**
     * 获取指定类的指定字段的setter
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Set<Method> findGetters(Class clazz, String fieldName) {
        if (containsFastGetterProxy(clazz, fieldName)) {
            return getFastGetterProxy(clazz, fieldName);
        }
        Set<Method> ret = new HashSet<>();
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (Method item : getAllGetters(clazz)) {
            String name = item.getName();
            if (name.equals("get" + methodName) || name.equals("is" + methodName)) {
                ret.add(item);
            }
        }
        setFastGetterProxy(clazz, fieldName, ret);
        return ret;
    }

    /**
     * 获取指定类的指定字段的getter
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Set<Method> findSetters(Class clazz, String fieldName) {
        if (containsFastSetterProxy(clazz, fieldName)) {
            return getFastSetterProxy(clazz, fieldName);
        }
        Set<Method> ret = new HashSet<>();
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (Method item : getAllSetters(clazz)) {
            String name = item.getName();
            if (name.equals("set" + methodName)) {
                ret.add(item);
            }
        }
        setFastSetterProxy(clazz, fieldName, ret);
        return ret;
    }

    /**
     * 获取指定类中的指定名称的字段
     * 本类未找到，则尝试从父类中查找
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field findField(Class clazz, String fieldName) {
        if (containsFastFieldProxy(clazz, fieldName)) {
            return getFastFieldProxy(clazz, fieldName);
        }
        Field field = null;
        Set<Field> fields = getAllFields(clazz);
        for (Field item : fields) {
            String name = item.getName();
            if (name.equals(fieldName)) {
                field = item;
                break;
            }
        }
        if (field == null) {
            Set<Field> force = forceAllFields(clazz);
            for (Field item : force) {
                String name = item.getName();
                if (name.equals(fieldName)) {
                    field = item;
                    break;
                }
            }
        }
        setFastFieldProxy(clazz, fieldName, field);
        return field;
    }

    /**
     * 判断方法是否是一个标准的getter
     *
     * @param method
     * @return
     */
    public static boolean methodIsGetter(Method method) {
        if (method.getParameterCount() != 0) {
            return false;
        }
        if (method.getReturnType().equals(Void.class)) {
            return false;
        }
        if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
            return false;
        }
        return true;
    }

    /**
     * 判断方法是否是一个标准的setter
     *
     * @param method
     * @return
     */
    public static boolean methodIsSetter(Method method) {
        if (method.getParameterCount() != 1) {
            return false;
        }
        if (!method.getName().startsWith("set")) {
            return false;
        }
        return true;
    }

    /**
     * 强制获取指定类的所有字段，包含父类中的私有字段
     *
     * @param clazz
     * @return
     */
    public static Set<Field> forceAllFields(Class clazz) {
        if (cacheForceFields.containsKey(clazz)) {
            return cacheForceFields.get(clazz);
        }
        Set<Field> ret = new HashSet<>();
        if (null == clazz) {
            return ret;
        }
        if (Object.class.equals(clazz)) {
            return ret;
        }
        Set<Field> fds = getAllFields(clazz);
        ret.addAll(fds);
        Class sclazz = clazz.getSuperclass();
        Set<Field> sfds = forceAllFields(sclazz);
        ret.addAll(sfds);
        cacheForceFields.put(clazz, ret);
        return ret;
    }

    /**
     * 检查元素elem是否具有指定的注解clazz
     * ckClass 指定是否检查类上面
     * ckSuperClass 指定是否检查父类上面
     * ckAnnotatedAnn 指定是否检查注解的注解
     *
     * @param elem
     * @param clazz
     * @param ckClass
     * @param ckSuperClass
     * @param ckAnnotatedAnn
     * @return
     */
    public static <T extends Annotation> T findElementAnnotation(AnnotatedElement elem, Class<T> clazz, boolean ckClass, boolean ckSuperClass, boolean ckAnnotatedAnn) {
        if(elem instanceof Class){
            Class cls=(Class)elem;
            if(Object.class.equals(cls)){
                return null;
            }
        }
        T ret = findAnnotation(elem, clazz, ckAnnotatedAnn);
        if (ret != null) {
            return ret;
        }
        Class locClass = null;
        if (elem instanceof AccessibleObject) {
            if (ckClass) {
                if (elem instanceof Field) {
                    Field field = (Field) elem;
                    locClass = field.getDeclaringClass();
                } else if (elem instanceof Method) {
                    Method method = (Method) elem;
                    locClass = method.getDeclaringClass();
                } else if (elem instanceof Constructor) {
                    Constructor cons = (Constructor) elem;
                    locClass = cons.getDeclaringClass();
                }
                ret = findElementAnnotation(locClass, clazz, ckClass, ckSuperClass, ckAnnotatedAnn);
                if (ret != null) {
                    return ret;
                }
            }
        } else if (elem instanceof Class) {
            locClass = (Class) elem;
            ret = findElementAnnotation(locClass, clazz, ckClass, ckSuperClass, ckAnnotatedAnn);
            if (ret != null) {
                return ret;
            }
        } else if (elem instanceof Parameter) {
            ret = findAnnotation(elem, clazz, ckAnnotatedAnn);
            if (ret != null) {
                return ret;
            }
        }
        if (ckSuperClass) {
            if (!Object.class.equals(locClass)) {
                Set<Class> supers = new HashSet<>();
                Class superClass = locClass.getSuperclass();
                supers.add(superClass);
                Class[] interfaces = locClass.getInterfaces();
                for (Class item : interfaces) {
                    supers.add(item);
                }
                for (Class item : supers) {
                    ret = findElementAnnotation(item, clazz, ckClass, ckSuperClass, ckAnnotatedAnn);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
        }
        return ret;
    }

    public static <T extends Annotation> T findAnnotation(AnnotatedElement elem, Class<T> clazz, boolean ckAnnotatedAnn) {
        Set<Annotation> anns = getAllAnnotations(elem);
        T ret = null;
        for (Annotation item : anns) {
            if (item.annotationType().equals(clazz)) {
                return (T) item;
            }
            if (ckAnnotatedAnn) {
                Class type = item.annotationType();
                Set<Annotation> superAnns = getAnnotatedAnnotationsNext(type);
                for (Annotation sitem : superAnns) {
                    if (sitem.annotationType().equals(clazz)) {
                        return (T) sitem;
                    }
                }
            }
        }
        return ret;
    }

    private static Set<Annotation> getAnnotatedAnnotationsNext(Class<? extends Annotation> annClazz) {
        Set<Annotation> ret = new HashSet<Annotation>();
        Set<Annotation> anns = getAllAnnotations(annClazz);
        for (Annotation item : anns) {
            String annName = item.annotationType().getName();
            if (annName.startsWith("java.lang.annotation.")) {
                continue;
            }
            ret.add(item);
            Set<Annotation> annotatedAnns = getAnnotatedAnnotationsNext(item.annotationType());
            ret.addAll(annotatedAnns);
        }
        return ret;
    }

    public static Set<Annotation> getAllAnnotations(AnnotatedElement elem) {
        Set<Annotation> ret = new HashSet<>();
        Annotation[] anns = elem.getAnnotations();
        for (Annotation item : anns) {
            ret.add(item);
        }
        Annotation[] danns = elem.getDeclaredAnnotations();
        for (Annotation item : danns) {
            ret.add(item);
        }
        return ret;
    }
}
