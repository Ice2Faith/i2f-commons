package i2f.commons.core.utils.reflect.core.resolver.base;


import i2f.commons.core.utils.safe.CheckUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AnnotationResolver {
    public static boolean hasIncludeAnnotations(Class<? extends Annotation>[] requireAnnos, Annotation[] annos) {
        if (CheckUtil.isExEmptyArray(annos, requireAnnos)) {
            return false;
        }
        for (Class<? extends Annotation> item : requireAnnos) {
            for (Annotation cur : annos) {
                Set<Annotation> parentAnnos = getAnnotatedAnnotations(cur);
                if (CheckUtil.isEmptyArray(parentAnnos)) {
                    continue;
                }
                for (Annotation curParent : parentAnnos) {
                    if (isTargetAnnotation(curParent, true, item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Class<? extends Annotation> getAnnotationType(Annotation ann) {
        return ann.annotationType();
    }

    public static boolean isTargetAnnotation(Annotation ann, boolean checkAnnotatedAnnotation, Class<? extends Annotation>... tarTypes) {
        if (CheckUtil.isEmptyArray(tarTypes)) {
            return false;
        }
        Class<? extends Annotation> ckType = getAnnotationType(ann);
        for (Class item : tarTypes) {
            if (ckType.equals(item)) {
                return true;
            }
            if (checkAnnotatedAnnotation) {
                Set<Annotation> anns = getAnnotatedAnnotations(ann);
                for (Annotation pann : anns) {
                    if (getAnnotationType(pann).equals(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Set<Annotation> getAnnotatedAnnotations(Annotation annotation) {
        Set<Annotation> ret = new HashSet<Annotation>();
        if (annotation == null) {
            return ret;
        }
        ret.add(annotation);
        Set<Annotation> annAnns = getAnnotatedAnnotationsNext(getAnnotationType(annotation));
        ret.addAll(annAnns);
        return ret;
    }

    private static Set<Annotation> getAnnotatedAnnotationsNext(Class<? extends Annotation> annClazz) {
        Set<Annotation> ret = new HashSet<Annotation>();
        Set<Annotation> anns = AnnotationResolver.getAnnotationsProxy(annClazz);
        for (Annotation item : anns) {
            String annName = item.annotationType().getName();
            if (annName.startsWith("java.lang.annotation.")) {
                continue;
            }
            ret.add(item);
            Set<Annotation> annotatedAnns = getAnnotatedAnnotationsNext(getAnnotationType(item));
            ret.addAll(annotatedAnns);
        }
        return ret;
    }

    public static Set<Annotation> getAnnotationsProxy(AnnotatedElement annotatedElement) {
        Annotation[] anns = annotatedElement.getAnnotations();
        Annotation[] danns = annotatedElement.getDeclaredAnnotations();
        Set<Annotation> set = new HashSet<Annotation>();
        for (Annotation item : anns) {
            set.add(item);
        }
        for (Annotation item : danns) {
            set.add(item);
        }
        return set;
    }

    public static Set<Annotation> getTargetAnnotationsProxy(AnnotatedElement annotatedElement, boolean checkAnnotatedAnnotation, Class<? extends Annotation>... annotationClasses) {
        Set<Annotation> anns = getAnnotationsProxy(annotatedElement);
        Set<Annotation> set = new HashSet<Annotation>();
        for (Annotation item : anns) {
            if (isTargetAnnotation(item, checkAnnotatedAnnotation, annotationClasses)) {
                set.add(item);
            }
        }
        return set;
    }

    public static Set<Annotation> getClassAnnotations(Class clazz, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<? extends Annotation>... annotationClasses) {
        if (clazz == null || Object.class.equals(clazz)) {
            return new HashSet<Annotation>();
        }
        Set<Annotation> anns = getTargetAnnotationsProxy(clazz, checkAnnotatedAnnotation, annotationClasses);
        if (checkSuperClass) {
            Class superClazz = clazz.getSuperclass();
            Set<Annotation> suAnns = getClassAnnotations(superClazz, checkSuperClass, checkAnnotatedAnnotation, annotationClasses);
            anns.addAll(suAnns);
            Class[] infClazzes = clazz.getInterfaces();
            for (Class item : infClazzes) {
                Set<Annotation> infAnns = getClassAnnotations(item, checkSuperClass, checkAnnotatedAnnotation, annotationClasses);
                anns.addAll(infAnns);
            }
        }

        return anns;
    }

    public static <T extends Annotation> T getClassAnnotation(Class clazz, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<T> annotationClass) {
        Set<Annotation> ann = getClassAnnotations(clazz, checkSuperClass, checkAnnotatedAnnotation, annotationClass);
        if (ann.size() > 0) {
            return (T) ann.iterator().next();
        }
        return null;
    }

    private static Set<Annotation> getClassInnerElementAnnotationsProxy(Class clazz, AnnotatedElement annotatedElement, boolean checkClass, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<? extends Annotation> ... annotationClasses) {
        Set<Annotation> ret=new HashSet<Annotation>();
        if (annotatedElement == null) {
            return ret;
        }
        Set<Annotation> anns = getTargetAnnotationsProxy(annotatedElement, checkAnnotatedAnnotation, annotationClasses);
        ret.addAll(anns);

        if (checkClass) {
            Set<Annotation> clazzAnns = getClassAnnotations(clazz,checkSuperClass,checkAnnotatedAnnotation,annotationClasses);
            ret.addAll(clazzAnns);
        }

        return ret;
    }

    public static Set<Annotation> getMethodAnnotations(Method method, boolean checkClass, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<? extends Annotation> ... annotationClasses) {
        return getClassInnerElementAnnotationsProxy(method.getDeclaringClass(), method, checkClass, checkSuperClass, checkAnnotatedAnnotation, annotationClasses);
    }

    public static Set<Annotation> getFieldAnnotations(Field field, boolean checkClass, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<? extends Annotation> ... annotationClasses) {
        return getClassInnerElementAnnotationsProxy(field.getDeclaringClass(), field, checkClass, checkSuperClass, checkAnnotatedAnnotation, annotationClasses);
    }

    public static Set<Annotation> getConstructorAnnotations(Constructor constructor, boolean checkClass, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<? extends Annotation> ... annotationClasses) {
        return getClassInnerElementAnnotationsProxy(constructor.getDeclaringClass(), constructor, checkClass, checkSuperClass, checkAnnotatedAnnotation, annotationClasses);
    }

    public static <T extends Annotation> T getMethodAnnotation(Method method, boolean checkClass, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<T> annotationClass) {
        Set<Annotation> set=getClassInnerElementAnnotationsProxy(method.getDeclaringClass(), method, checkClass, checkSuperClass, checkAnnotatedAnnotation, annotationClass);
        if(set.size()>0){
            return (T)set.iterator().next();
        }
        return null;
    }

    public static <T extends Annotation> T getFieldAnnotation(Field field, boolean checkClass, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<T> annotationClass) {
        Set<Annotation> set=getClassInnerElementAnnotationsProxy(field.getDeclaringClass(), field, checkClass, checkSuperClass, checkAnnotatedAnnotation, annotationClass);
        if(set.size()>0){
            return (T)set.iterator().next();
        }
        return null;
    }

    public static <T extends Annotation> T getConstructorAnnotation(Constructor constructor, boolean checkClass, boolean checkSuperClass, boolean checkAnnotatedAnnotation, Class<T> annotationClass) {
        Set<Annotation> set=getClassInnerElementAnnotationsProxy(constructor.getDeclaringClass(), constructor, checkClass, checkSuperClass, checkAnnotatedAnnotation, annotationClass);
        if(set.size()>0){
            return (T)set.iterator().next();
        }
        return null;
    }

    public static <T extends Annotation> T getMethodAnnotation(Method method, Class<T> annotationClass) {
        return getMethodAnnotation(method, true, true, true, annotationClass);
    }

    public static <T extends Annotation> T getFieldAnnotation(Field field, Class<T> annotationClass) {
        return getFieldAnnotation(field, false, false, true, annotationClass);
    }

    public static <T extends Annotation> T getConstructorAnnotation(Constructor constructor, Class<T> annotationClass) {
        return getConstructorAnnotation(constructor, true, false, true, annotationClass);
    }


}
