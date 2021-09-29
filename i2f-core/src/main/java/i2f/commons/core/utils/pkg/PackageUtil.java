package i2f.commons.core.utils.pkg;

import i2f.commons.core.utils.pkg.data.ClassMetaData;
import i2f.commons.core.utils.pkg.filter.AbstractClassFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PackageUtil {

    public static List<Class> getIncludeAnnotationClasses(String basePackage, Class<? extends Annotation>... annos) {
        List<Class> ret = new ArrayList<>();


        List<ClassMetaData> list = PackageScanner.scanClasses(new AbstractClassFilter() {
            @Override
            public int onMaskCode() {
                return maskCode(FilterType.CLASS_ANNOTATIONS);
            }

            @Override
            public boolean onClassAnnotation(Set<Annotation> annotations) {
                return hasIncludeAnnotations(annotations, annos);
            }
        }, basePackage);
        for (ClassMetaData item : list) {
            ret.add(item.getClazz());
        }


        return ret;
    }

    public static List<Class> getIncludeAnnotationsFieldsClasses(String basePackage, Class<? extends Annotation>... annos) {
        List<Class> ret = new ArrayList<>();


        List<ClassMetaData> list = PackageScanner.scanClasses(new AbstractClassFilter() {
            @Override
            public boolean onFieldAnnotations(Field field, Set<Annotation> annotations) {
                return hasIncludeAnnotations(annotations, annos);
            }

            @Override
            public int onMaskCode() {
                return maskCode(FilterType.FIELD_ANNOTATIONS);
            }
        }, basePackage);
        for (ClassMetaData item : list) {
            ret.add(item.getClazz());
        }


        return ret;
    }

    public static List<Class> getIncludeAnnotationsMethodsClasses(String basePackage, Class<? extends Annotation>... annos) {
        List<Class> ret = new ArrayList<>();


            List<ClassMetaData> list = PackageScanner.scanClasses(new AbstractClassFilter() {
                @Override
                public boolean onMethodAnnotations(Method method, Set<Annotation> annotations) {
                    return hasIncludeAnnotations(annotations,annos);
                }

                @Override
                public int onMaskCode() {
                    return maskCode(FilterType.METHOD_ANNOTATIONS);
                }
            }, basePackage);
            for (ClassMetaData item : list) {
                ret.add(item.getClazz());
            }


        return ret;
    }
}
