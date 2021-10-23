package i2f.commons.core.utils.reflect.core.resolver.base;

import i2f.commons.core.utils.safe.CheckUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ConvertResolver {
    public static boolean canConvert2Number(Object srcObj) {
        return (srcObj instanceof String && CheckUtil.isFloatNumber((String) srcObj))
                || canConvert2(BigInteger.class, srcObj)
                || canConvert2(BigDecimal.class, srcObj);
    }

    public static BigDecimal convert2Number(Object srcObj) {
        if (srcObj instanceof String && CheckUtil.isFloatNumber((String) srcObj)) {
            return new BigDecimal((String) srcObj);
        }
        if (canConvert2(BigInteger.class, srcObj)) {
            BigInteger iv = (BigInteger) tryConvertType(BigInteger.class, srcObj);
            BigDecimal dv = new BigDecimal(iv.toString());
            return dv;
        }
        return (BigDecimal) tryConvertType(BigDecimal.class, srcObj);
    }

    public static <E> boolean canConvert2(Class dstType, E srcObj) {
        if (srcObj == null) {
            return true;
        }
        if (TypeResolver.isTargetType(dstType, Object.class)) {
            return true;
        }
        Class srcType = srcObj.getClass();
        if (TypeResolver.isSameType(dstType, srcType)) {
            return true;
        }
        if (TypeResolver.isTargetType(dstType, String.class)) {
            return true;
        }
        if (TypeResolver.isTargetType(dstType, Date.class, java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class)
                && TypeResolver.isTargetType(srcType, Date.class, java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class)) {
            return true;
        }

        if (TypeResolver.isTargetType(dstType, byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                char.class, Character.class,
                BigInteger.class)
                && TypeResolver.isTargetType(srcType, byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                char.class, Character.class,
                BigInteger.class)) {
            return true;
        }

        if (TypeResolver.isTargetType(dstType, float.class, Float.class,
                double.class, Double.class,
                BigDecimal.class)
                && TypeResolver.isTargetType(srcType, float.class, Float.class,
                double.class, Double.class,
                BigDecimal.class)) {
            return true;
        }

        if (TypeResolver.isTargetType(dstType, boolean.class, Boolean.class)
                && TypeResolver.isTargetType(srcType, boolean.class, Boolean.class, String.class)) {
            return true;
        }


        return false;
    }

    public static <E> Object tryConvertType(Class<E> dstType, Object srcObj) {
        if (srcObj == null) {
            return srcObj;
        }
        if (TypeResolver.isTargetType(dstType, Object.class)) {
            return srcObj;
        }
        Class srcType = srcObj.getClass();
        if (TypeResolver.isSameType(dstType, srcType)) {
            return srcObj;
        }
        if (TypeResolver.isTargetType(dstType, String.class)) {
            return String.valueOf(srcObj);
        }
        if (TypeResolver.isTargetType(dstType, Date.class, java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class)
                && TypeResolver.isTargetType(srcType, Date.class, java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class)) {
            long timest = ((Date) srcObj).getTime();
            if (TypeResolver.isTargetType(dstType, Date.class)) {
                return new Date(timest);
            } else if (TypeResolver.isTargetType(dstType, java.sql.Timestamp.class)) {
                return new java.sql.Timestamp(timest);
            } else if (TypeResolver.isTargetType(dstType, java.sql.Date.class)) {
                return new java.sql.Date(timest);
            } else if (TypeResolver.isTargetType(dstType, java.sql.Time.class)) {
                return new java.sql.Time(timest);
            }
        }

        if (TypeResolver.isTargetType(dstType, byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                char.class, Character.class,
                BigInteger.class)
                && TypeResolver.isTargetType(srcType, byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                char.class, Character.class,
                BigInteger.class)) {
            BigInteger val = null;
            if (TypeResolver.isTargetType(srcType, Character.class, char.class)) {
                int cval = (int) (char) (Character) srcObj;
                val = new BigInteger(String.valueOf(cval));
            } else {
                val = new BigInteger(String.valueOf(srcObj));
            }

            if (TypeResolver.isTargetType(dstType, byte.class, Byte.class)) {
                return val.byteValue();
            } else if (TypeResolver.isTargetType(dstType, short.class, Short.class)) {
                return val.shortValue();
            } else if (TypeResolver.isTargetType(dstType, int.class, Integer.class)) {
                return val.intValue();
            } else if (TypeResolver.isTargetType(dstType, long.class, Long.class)) {
                return val.longValue();
            } else if (TypeResolver.isTargetType(dstType, char.class, Character.class)) {
                return (char) val.intValue();
            } else if (TypeResolver.isTargetType(dstType, BigInteger.class)) {
                return val;
            }
            return srcObj;
        }

        if (TypeResolver.isTargetType(dstType, float.class, Float.class,
                double.class, Double.class,
                BigDecimal.class)
                && TypeResolver.isTargetType(srcType, float.class, Float.class,
                double.class, Double.class,
                BigDecimal.class)) {
            BigDecimal val = new BigDecimal(String.valueOf(srcObj));
            if (TypeResolver.isTargetType(dstType, float.class, Float.class)) {
                return val.floatValue();
            } else if (TypeResolver.isTargetType(dstType, double.class, Double.class)) {
                return val.doubleValue();
            } else if (TypeResolver.isTargetType(dstType, BigDecimal.class)) {
                return val;
            }
            return srcObj;
        }

        return srcObj;
    }
}
