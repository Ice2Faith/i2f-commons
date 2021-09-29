package i2f.commons.core.utils.pkg.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ltb
 * @date 2021/9/23
 */
public class AnnotationUtil {
    public static Set<Annotation> getElemAnnotations(AnnotatedElement elem){
        Annotation[] anns=new Annotation[0];
        Annotation[] danns=new Annotation[0];
        try{
            anns= elem.getAnnotations();
        }catch(Throwable e){

        }
        try{
            danns=elem.getDeclaredAnnotations();
        }catch(Throwable e){

        }
        Set<Annotation> ret=new HashSet<>(8);
        for(Annotation item : anns){
            ret.add(item);
        }
        for(Annotation item : danns){
            ret.add(item);
        }
        return ret;
    }

    public static <T> Set<T> mergeElem(T[] arr1,T[] arr2){
        Set<T> ret=new HashSet<>((int)((arr1.length+ arr2.length)/0.7));
        for(T item : arr1){
            ret.add(item);
        }
        for(T item : arr2){
            ret.add(item);
        }
        return ret;
    }

}
