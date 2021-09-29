package i2f.commons.core.validator;


import i2f.commons.core.validator.core.ValidateWorker;
import i2f.commons.core.validator.exception.ValidateException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ValidateDriver {
    public static void test(Annotation[] annos,Object val)throws ValidateException {
        ValidateWorker.test(annos,val);
    }
    public static void test(Parameter param,Object val)throws ValidateException{
        ValidateWorker.test(param, val);
    }
    public static void test(Field field, Object val)throws ValidateException{
        ValidateWorker.test(field, val);
    }
    public static void test(Method method,Object[] args)throws ValidateException{
        ValidateWorker.test(method, args);
    }
    public static<T> void testBean(T obj)throws ValidateException{
        ValidateWorker.testBean(obj);
    }
}
