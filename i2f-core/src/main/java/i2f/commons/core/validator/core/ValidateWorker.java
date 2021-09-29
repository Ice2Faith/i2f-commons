package i2f.commons.core.validator.core;


import i2f.commons.core.validator.IProxyBeanValidateHandler;
import i2f.commons.core.validator.IProxyValidateHandler;
import i2f.commons.core.validator.annotations.*;
import i2f.commons.core.validator.exception.ValidateException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidateWorker {
    public static Annotation[] getSupportValidateAnnotations(Annotation[] annotations){
        ArrayList<Annotation> ret=new ArrayList<>();
        for(Annotation item : annotations){
            if(isSupportValidateAnnotation(item)){
                ret.add(item);
            }
        }
        return (Annotation[]) ret.toArray();
    }
    public static boolean isSupportValidateAnnotation(Annotation anno){
        if (anno instanceof Vnull
                ||anno instanceof Vregex
                ||anno instanceof VnullEmpty
                ||anno instanceof Vint
                ||anno instanceof Vdouble
                ||anno instanceof Vstring
                ||anno instanceof Vbean
                ||anno instanceof Vproxy
                ||anno instanceof VproxyBean) {
            return true;
        }
        return false;
    }
    public static void test(Annotation[] annos, Object val) throws ValidateException {
        for (Annotation anno : annos) {
            if (anno instanceof Vnull) {
                testNull((Vnull) anno, val);
            } else if (anno instanceof Vregex) {
                testRegex((Vregex) anno, val);
            } else if (anno instanceof VnullEmpty) {
                testNullEmpty((VnullEmpty) anno, val);
            } else if (anno instanceof Vint) {
                testInteger((Vint) anno, val);
            } else if (anno instanceof Vdouble) {
                testDouble((Vdouble) anno, val);
            } else if (anno instanceof Vstring) {
                testString((Vstring) anno, val);
            } else if (anno instanceof Vbean) {
                Vbean validator=(Vbean)anno;
                if(validator.checkNull() && val==null){
                    ValidateException.thrThrow(validator.nullMsg(),null);
                }else{
                    testBean(val);
                }
            } else if (anno instanceof Vproxy) {
                testProxy((Vproxy) anno, val);
            } else if (anno instanceof VproxyBean) {
                testBeanProxy((VproxyBean) anno, val);
            }
        }
    }

    public static void test(Parameter param, Object val) throws ValidateException {
        Annotation[] annos = param.getDeclaredAnnotations();
        test(annos, val);
    }

    public static void test(Field field, Object val) throws ValidateException {
        Annotation[] annos = field.getDeclaredAnnotations();
        test(annos, val);
    }

    public static void test(Method method, Object[] args) throws ValidateException {
        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            test(params[i], args[i]);
        }
    }

    public static void testBean(Object bean) throws ValidateException {
        if (bean == null) {
            return;
        }
        Class clazz = bean.getClass();
        Field[] fields = clazz.getFields();
        if (fields == null || fields.length == 0) {
            return;
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                field.setAccessible(true);
                Object val = field.get(bean);
                test(field, val);
            } catch (IllegalAccessException | SecurityException e) {

            }
        }
    }


    public static void testRegex(Vregex validator, Object val) throws ValidateException {
        if (val == null) {
            if(validator.checkNull()) {
                throw ValidateException.thr(validator.exMsg(),val);
            }else{
                return;
            }
        }
        if (val instanceof String) {
            String str = (String) val;
            String regex = validator.value();
            if (!str.matches(regex)) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }
    }

    public static void testNull(Vnull validator, Object val) throws ValidateException {
        if (val == null) {
            ValidateException.thrThrow(validator.exMsg(),val);
        }
    }

    public static void testNullEmpty(VnullEmpty validator, Object val) throws ValidateException {
        if (val == null) {
            throw ValidateException.thr(validator.exMsg(),val);
        }
        if (val instanceof String) {
            String str = (String) val;
            if (str.length() == 0) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        } else if (val.getClass().isArray() && Array.getLength(val)==0) {
            throw ValidateException.thr(validator.exMsg(),val);
        } else if (val instanceof Collection) {
            Collection collect = (Collection) val;
            if (collect.size() == 0) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        } else if (val instanceof Map) {
            Map map = (Map) val;
            if (map.size() == 0) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }
    }

    public static void testInteger(Vint validator, Object val) throws ValidateException {
        if (val == null) {
            if(validator.checkNull()) {
                throw ValidateException.thr(validator.exMsg(),val);
            }else{
                return;
            }
        }
        try {
            String str = val + "";

            if(str.length()==0 && !validator.forceNum()){
                return;
            }

            BigInteger ival=new BigInteger(str);

            String smin = validator.min();
            String smax = validator.max();
            if (smax != null && !"".equals(smax)) {
                BigInteger max=new BigInteger(smax);
                if (max.compareTo(ival) <= 0) {
                    throw ValidateException.thr(validator.exMsg(),val);
                }
            }
            if (smin != null && !"".equals(smin)) {
                BigInteger min=new BigInteger(smin);
                if (min.compareTo(ival) >= 0) {
                    throw ValidateException.thr(validator.exMsg(),val);
                }
            }
        } catch (NumberFormatException e) {
            throw ValidateException.cat(validator.exMsg(), e,val);
        }
    }

    public static void testDouble(Vdouble validator, Object val) throws ValidateException {
        if (val == null) {
            if(validator.checkNull()) {
                throw ValidateException.thr(validator.exMsg(),val);
            }else{
                return;
            }
        }
        try {
            String str = val + "";

            if(str.length()==0 && !validator.forceNum()){
                return;
            }

            BigDecimal dval=new BigDecimal(str);

            String smin = validator.min();
            String smax = validator.max();
            if (smax != null && !"".equals(smax)) {
                BigDecimal max=new BigDecimal(smax);
                if (max.compareTo(dval) <= 0) {
                    throw ValidateException.thr(validator.exMsg(),val);
                }
            }
            if (smin != null && !"".equals(smin)) {
                BigDecimal min=new BigDecimal(smin);
                if (min.compareTo(dval) >= 0) {
                    throw ValidateException.thr(validator.exMsg(),val);
                }
            }
        } catch (NumberFormatException e) {
            throw ValidateException.cat(validator.exMsg(),e,val);
        }
    }

    public static void testString(Vstring validator, Object val) throws ValidateException {
        boolean bCkNull=validator.checkNull();
        boolean bNotEmpty=validator.notEmpty();
        boolean bNeedTrim=validator.needTrim();

        if(validator.nlne()){
            bCkNull=true;
            bNotEmpty=true;
        }

        if(validator.tnlne()){
            bCkNull=true;
            bNeedTrim=true;
            bNotEmpty=true;
        }

        if (val == null) {
            if(validator.checkNull()) {
                throw ValidateException.thr(validator.exMsg(),val);
            }else{
                return;
            }
        }

        String str = (String) val;

        if (bNeedTrim) {
            str = str.trim();
        }

        int strLen = str.length();

        if (bNotEmpty && strLen == 0) {
            throw ValidateException.thr(validator.exMsg(),val);
        }

        if (validator.lengthEqual() >= 0 && strLen != validator.lengthEqual()) {
            throw ValidateException.thr(validator.exMsg(),val);
        }

        if (validator.lengthMin() >= 0 && strLen < validator.lengthMin()) {
            throw ValidateException.thr(validator.exMsg(),val);
        }

        if (validator.lengthMax() >= 0 && strLen > validator.lengthMax()) {
            throw ValidateException.thr(validator.exMsg(),val);
        }

        if (!"".equals(validator.regexMatch())) {
            if (str.matches(validator.regexMatch())) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }

        if (!"".equals(validator.regexNotMatch())) {
            if (!str.matches(validator.regexNotMatch())) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }

        if (!"".equals(validator.contains())) {
            if (str.contains(validator.contains())) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }

        if (!"".equals(validator.notContains())) {
            if (!str.contains(validator.notContains())) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }

        if (!"".equals(validator.startWith())) {
            if (str.startsWith(validator.startWith())) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }

        if (!"".equals(validator.notStartWith())) {
            if (!str.startsWith(validator.notStartWith())) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }

        if (!"".equals(validator.endWith())) {
            if (str.endsWith(validator.endWith())) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }

        if (!"".equals(validator.notEndWith())) {
            if (!str.endsWith(validator.notEndWith())) {
                throw ValidateException.thr(validator.exMsg(),val);
            }
        }

    }

    private static ConcurrentHashMap<Class<?>, IProxyHandler> handlerContainer=new ConcurrentHashMap<>();
    private static IProxyHandler save2Container(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        if(handlerContainer.contains(clazz)){
            return handlerContainer.get(clazz);
        }
        IProxyHandler handler=(IProxyHandler)clazz.newInstance();
        handlerContainer.put(clazz,handler);
        return handler;
    }

    public static void testProxy(Vproxy validator, Object val) throws ValidateException {
        Class<?> clazz = validator.value();
        try {
            IProxyValidateHandler handler = (IProxyValidateHandler)save2Container(clazz);
            handler.test(validator, val);
        } catch (InstantiationException | IllegalAccessException e) {

        }
    }

    public static void testBeanProxy(VproxyBean validator, Object bean) throws ValidateException {
        Class<?> clazz = validator.value();
        try {
            IProxyBeanValidateHandler handler = (IProxyBeanValidateHandler)save2Container(clazz);
            handler.test(validator, bean);
        } catch (InstantiationException | IllegalAccessException e) {

        }
    }
}
