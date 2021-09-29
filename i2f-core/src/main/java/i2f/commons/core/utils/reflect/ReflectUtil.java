package i2f.commons.core.utils.reflect;


import i2f.commons.core.utils.data.DataUtil;
import i2f.commons.core.utils.reflect.core.resolver.base.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class ReflectUtil {

    public static boolean isSameType(Class<?> t1, Class<?> t2) {
        return TypeResolver.isSameType(t1, t2);
    }

    public static Method matchMethod(Class clazz, String methodName, Class<?>... paramTypes) {
        return MethodResolver.matchMethod(clazz, methodName, paramTypes);
    }

    public static boolean hasIncludeAnnotations(Class<? extends Annotation>[] requireAnnos, Annotation[] annos) {
        return AnnotationResolver.hasIncludeAnnotations(requireAnnos, annos);
    }

    public static boolean hasAnnotationsField(Field field, Class<? extends Annotation>... requireAnnos) {
        Annotation[] annos = field.getAnnotations();
        return hasIncludeAnnotations(requireAnnos, annos);
    }

    public static boolean hasAnnotationsMethod(Method method, Class<? extends Annotation>... requireAnnos) {
        Annotation[] annos = method.getAnnotations();
        return hasIncludeAnnotations(requireAnnos, annos);
    }

    public static boolean hasAnnotationsClass(Class clazz, Class<? extends Annotation>... requireAnnos) {
        Annotation[] annos = clazz.getAnnotations();
        return hasIncludeAnnotations(requireAnnos, annos);
    }

    public static Field[] getAllFields(Class clazz) {
        return DataUtil.toArr(FieldResolver.getAllFields(clazz,false),Field[].class);
    }
    public static Field[] getAllFields(Class clazz,boolean trimStaticFinal) {
        return DataUtil.toArr(FieldResolver.getAllFields(clazz,trimStaticFinal),Field[].class);
    }
    public static Set<Field> getForceAllFields(Class clazz,boolean trimStaticFinal){
        return FieldResolver.getForceAllFields(clazz,trimStaticFinal);
    }
    public static boolean isStaticFinal(Field field){
        return FieldResolver.isStaticFinal(field);
    }
    public static boolean isStaticFinal(int modifier){
        return FieldResolver.isStaticFinal(modifier);
    }

    public static Method[] getAllMethods(Class clazz) {
        return DataUtil.toArr(MethodResolver.getAllMethods(clazz),Method[].class);
    }

    public static List<Field> getHasAnnotationsFields(Class clazz, Class<? extends Annotation>... requireAnnos) {
        Set<Field> set=FieldResolver.getAllFieldsWithAnnotations(clazz,true,false,requireAnnos);
        return DataUtil.toList(set);
    }

    public static List<Method> getHasAnnotationsMethods(Class clazz, Class<? extends Annotation>... requireAnnos) {
        Set<Method> set=MethodResolver.getAllMethodsWithAnnotations(clazz,requireAnnos);
        return DataUtil.toList(set);
    }

    public static Class getClazz(String className) {
        return ClassResolver.getClazz(className);
    }

    public static <T> Class<T> getClazz(T obj) {
        return ClassResolver.getClazz(obj);
    }

    public static List<Field> getFields(Class clazz, boolean ignoreCase, String... fieldNames) {
        return FieldResolver.getFields(clazz, ignoreCase, fieldNames);
    }

    public static List<Method> getMethods(Class clazz, boolean ignoreCase, String... methodNames) {
        return MethodResolver.getMethods(clazz, ignoreCase, methodNames);
    }


    public static List<Annotation> getAnnotations(Field field,Class<? extends Annotation> ... annosClasses){
        Set<Annotation> set=AnnotationResolver.getFieldAnnotations(field,false,false,true,annosClasses);
        return DataUtil.toList(set);
    }

    public static List<Annotation> getAnnotations(Method method,Class<? extends Annotation> ... annosClasses){
        Set<Annotation> set=AnnotationResolver.getMethodAnnotations(method,true,true,true,annosClasses);
        return DataUtil.toList(set);
    }

    public static List<Annotation> getAnnotations(Class clazz,Class<? extends Annotation> ... annosClasses){
        Set<Annotation> set=AnnotationResolver.getClassAnnotations(clazz,true,true,annosClasses);
        return DataUtil.toList(set);
    }


    /**
     * 获取指定类的指定注解
     * @param clazz 目标类
     * @param requireAnns 需要的注解，可以为null
     * @param ckSuperClass 是否检查其父类，true时将会检查到Object
     * @param ckInterfaces 是否检查其实现的接口，true时会检查所有实现的接口
     * @param ckAnnosAnn 是否检查需要的注解的注解，也就是注解上的注解，比如@Controller注解上是有@Component注解的，因此你可以扫描@Component注解，这样就能够扫描到@Controller注解，注意，这里只会检查注解的注解，也就是一层，不会无限递归
     * @return
     */
    public static List<Annotation> getClassAnnotations(Class clazz,Class<? extends Annotation>[] requireAnns,boolean ckSuperClass,boolean ckInterfaces,boolean ckAnnosAnn){
        Set<Annotation> set=AnnotationResolver.getClassAnnotations(clazz,ckSuperClass,ckAnnosAnn,requireAnns);
        return DataUtil.toList(set);

    }

    public static Annotation[] getAnnotatedElementAllAnnotations(AnnotatedElement elem){
        Set<Annotation> set=AnnotationResolver.getAnnotationsProxy(elem);
        return DataUtil.toArr(set,Annotation[].class);
    }

    public static<T> T instance(Class<T> clazz,Object ... args){
       return ClassResolver.instance(clazz,args);
    }

    public static String getter(String fieldName) {
        return MethodResolver.getter(fieldName);
    }

    public static String setter(String fieldName) {
        return MethodResolver.setter(fieldName);
    }

    public static Method findGetter(Class clazz, String fieldName) {
        return MethodResolver.findGetter(clazz,fieldName);
    }

    public static Method findSetter(Class clazz, String fieldName, Class<?> paramType) {
        return MethodResolver.findSetter(clazz, fieldName, paramType);
    }

    public static Method findGetter(Class clazz, Field field) {
        return findGetter(clazz, field.getName());
    }

    public static Method findSetter(Class clazz, Field field) {
        return findSetter(clazz, field.getName(), field.getType());
    }

    public static <E, T> ValueResolver.RetValResult<T> getValByGetter(E obj, Class clazz, String fieldName, boolean ignoreCase) {
        return ValueResolver.getValByGetter(obj,clazz,fieldName,ignoreCase);
    }

    public static <E, T> ValueResolver.RetValResult<T> getValByForce(E obj, Class clazz, String fieldName, boolean ignoreCase) {
        return ValueResolver.getValByForce(obj,clazz,fieldName,ignoreCase);
    }

    public static <T, E> ValueResolver.RetValResult<T> getValFull(E obj,Class clazz, String fieldName, boolean ignoreCase) {
        return ValueResolver.getValFull(obj,clazz,fieldName,ignoreCase);
    }

    public static <T, E> ValueResolver.RetValResult<T> getValFull(E obj, String fieldName, boolean ignoreCase) {
        return ValueResolver.getValFull(obj,fieldName,ignoreCase);
    }
    public static <T, E> T getVal(E obj,Class clazz, String fieldName, boolean ignoreCase) {
        return ValueResolver.getVal(obj,clazz,fieldName,ignoreCase);
    }
    public static <T, E> T getVal(E obj, String fieldName, boolean ignoreCase) {
        return ValueResolver.getVal(obj,fieldName,ignoreCase);
    }

    public static <E, T> boolean setValBySetter(E obj, Class clazz, String fieldName, T val, boolean ignoreCase) {
        return ValueResolver.setValBySetter(obj,clazz,fieldName,val,ignoreCase);
    }

    public static <E, T> boolean setValByForce(E obj, Class clazz, String fieldName, T val, boolean ignoreCase) {
        return ValueResolver.setValByForce(obj,clazz,fieldName,val,ignoreCase);
    }

    public static <E, T> boolean setVal(E obj, Class clazz, String fieldName, T val, boolean ignoreCase) {
        return ValueResolver.setVal(obj,clazz,fieldName,val,ignoreCase);
    }

    public static <E, T> boolean setVal(E obj, String fieldName, T val, boolean ignoreCase) {
        return ValueResolver.setVal(obj,fieldName,val,ignoreCase);
    }

    public static Class<? extends Annotation> getAnnotationType(Annotation ann){
        return AnnotationResolver.getAnnotationType(ann);
    }

    public static boolean isTargetAnnotation(Annotation ann,Class ... tarTypes){
        return AnnotationResolver.isTargetAnnotation(ann,true,tarTypes);
    }

    public static boolean isTargetType(Class ckType,Class ... tarTypes){
        return TypeResolver.isTargetType(ckType, tarTypes);
    }


    public static<E> boolean canConvert2(Class dstType,E srcObj){
        return ConvertResolver.canConvert2(dstType,srcObj);
    }

    public static<E> Object tryConvertType(Class dstType,E srcObj){
        return ConvertResolver.tryConvertType(dstType,srcObj);
    }

    /**
     * 获取方法上的目标注解，如果方法上没有此注解，
     * 尝试从方法所在的类上找目标注解
     * 这是符合使用常理的
     * @param method
     * @param clazz
     * @param <T>
     * @return
     */
    public static<T extends Annotation> T getMethodAnnotationCheckClass(Method method,Class<T> clazz){
        return AnnotationResolver.getMethodAnnotation(method,true,true,true,clazz);
    }
    public static<T extends Annotation> T getFieldAnnotationCheckClass(Field field,Class<T> clazz){
        return AnnotationResolver.getFieldAnnotation(field,true,true,true,clazz);
    }
}
