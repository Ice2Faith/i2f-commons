package i2f.commons.core.utils.reflect.core.resolver.base;

import i2f.commons.core.utils.safe.CheckUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ValueResolver {
    public static class RetValResult<E> {
        public E data;
        public boolean isSuccess;

        public RetValResult() {

        }

        public RetValResult(E data, boolean isSuccess) {
            this.data = data;
            this.isSuccess = isSuccess;
        }
    }

    public static <E, T> RetValResult<T> getValByGetter(E obj, Class clazz, String fieldName, boolean ignoreCase) {
        Method method = MethodResolver.findGetter(clazz, fieldName);
        if (method != null) {
            try {
                T ret = (T) method.invoke(obj);
                return new RetValResult<T>(ret, true);
            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        } else {
            List<Method> methods = MethodResolver.getMethods(clazz, ignoreCase, MethodResolver.getter(fieldName));
            for (Method item : methods) {
                if (item.getParameterCount() != 0) {
                    continue;
                }
                try {
                    T ret = (T) item.invoke(obj);
                    return new RetValResult<T>(ret, true);
                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {

                }
            }
        }
        return new RetValResult<T>(null, false);
    }

    public static <E, T> RetValResult<T> getValByForce(E obj, Class clazz, String fieldName, boolean ignoreCase) {
        List<Field> fields = FieldResolver.getFields(clazz, ignoreCase, fieldName);
        if (CheckUtil.isEmptyCollection(fields)) {
            Class superClass = clazz.getSuperclass();
            if (!Object.class.equals(superClass)) {
                return getValByForce(obj, superClass, fieldName, ignoreCase);
            }
            return new RetValResult<T>(null, false);
        }
        Field item = fields.get(0);
        try {
            item.setAccessible(true);
            T ret = (T) item.get(obj);
            return new RetValResult<T>(ret, true);
        } catch (IllegalAccessException e) {

        }


        return new RetValResult<T>(null, false);
    }

    public static <T, E> RetValResult<T> getValFull(E obj, Class clazz, String fieldName, boolean ignoreCase) {
        RetValResult<T> result = getValByGetter(obj, clazz, fieldName, ignoreCase);
        if (result.isSuccess) {
            return result;
        }
        result = getValByForce(obj, clazz, fieldName, ignoreCase);

        return result;
    }

    public static <T, E> RetValResult<T> getValFull(E obj, String fieldName, boolean ignoreCase) {
        Class<E> clazz = ClassResolver.getClazz(obj);
        return getValFull(obj, clazz, fieldName, ignoreCase);
    }

    public static <T, E> T getVal(E obj, Class clazz, String fieldName, boolean ignoreCase) {
        RetValResult<T> result = getValFull(obj, clazz, fieldName, ignoreCase);
        return result.data;
    }

    public static <T, E> T getVal(E obj, String fieldName, boolean ignoreCase) {
        RetValResult<T> result = getValFull(obj, fieldName, ignoreCase);
        return result.data;
    }

    public static <E, T> boolean setValBySetter(E obj, Class clazz, String fieldName, T val, boolean ignoreCase) {
        boolean ret = false;
        Method method = null;
        if (CheckUtil.notNull(val)) {
            method = MethodResolver.findSetter(clazz, fieldName, val.getClass());
        }
        if (method != null) {
            try {
                method.invoke(obj, val);
                ret = true;
            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        } else {
            List<Method> methods = MethodResolver.getMethods(clazz, ignoreCase, MethodResolver.setter(fieldName));
            for (Method item : methods) {
                if (item.getParameterCount() != 1) {
                    continue;
                }
                Class<?> paramType = item.getParameterTypes()[0];
                if (CheckUtil.isNull(val) || ConvertResolver.canConvert2(paramType, val)) {
                    try {
                        item.invoke(obj, ConvertResolver.tryConvertType(paramType, val));
                        ret = true;
                        break;
                    } catch (IllegalAccessException e) {

                    } catch (InvocationTargetException e) {

                    }
                } else {
                    try {
                        item.invoke(obj, val);
                        ret = true;
                        break;
                    } catch (Exception e) {

                    }
                }
            }
        }
        return ret;
    }

    public static <E, T> boolean setValByForce(E obj, Class clazz, String fieldName, T val, boolean ignoreCase) {
        List<Field> fields = FieldResolver.getFields(clazz, ignoreCase, fieldName);
        if (CheckUtil.isEmptyCollection(fields)) {
            Class superClass = clazz.getSuperclass();
            if (!Object.class.equals(superClass)) {
                return setValByForce(obj, superClass, fieldName, val, ignoreCase);
            }
            return false;
        }
        for (Field item : fields) {
            if (CheckUtil.isNull(val) || ConvertResolver.canConvert2(item.getType(), val)) {
                try {
                    item.setAccessible(true);
                    item.set(obj, ConvertResolver.tryConvertType(item.getType(), val));
                    return true;
                } catch (IllegalAccessException e) {

                }
                break;
            } else {
                try {
                    item.setAccessible(true);
                    item.set(obj, val);
                    return true;
                } catch (Exception e) {

                }
            }
        }
        return false;
    }

    public static <E, T> boolean setVal(E obj, Class clazz, String fieldName, T val, boolean ignoreCase) {
        boolean isSuccess = setValBySetter(obj, clazz, fieldName, val, ignoreCase);
        if (isSuccess) {
            return true;
        }
        isSuccess = setValByForce(obj, clazz, fieldName, val, ignoreCase);

        return isSuccess;
    }

    public static <E, T> boolean setVal(E obj, String fieldName, T val, boolean ignoreCase) {
        Class<E> clazz = ClassResolver.getClazz(obj);
        return setVal(obj, clazz, fieldName, val, ignoreCase);
    }

}
