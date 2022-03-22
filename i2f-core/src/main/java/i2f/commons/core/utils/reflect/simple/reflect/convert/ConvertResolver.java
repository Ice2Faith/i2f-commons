package i2f.commons.core.utils.reflect.simple.reflect.convert;


import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author ltb
 * @date 2022/3/14 11:24
 * @desc
 */
public class ConvertResolver {
    public static boolean isInTypes(Class ckType,Class ... tarTypes){
        if(ckType==null){
            return false;
        }
        if(tarTypes==null || tarTypes.length==0){
            return false;
        }
        for(Class item : tarTypes){
            if(ckType.equals(item)){
                return true;
            }
            //该方法用于判定，父类target是否派生出了子类item
            if(item.isAssignableFrom(ckType)){
                return true;
            }
        }
        return false;
    }

    public static boolean isValueConvertible(Object val,Class dstType){
        if(val==null){
            return true;
        }
        Class clazz=val.getClass();
        if(val instanceof String){
            String sval=(String)val;
            if(isInteger(dstType) && sval.matches("^\\d+$")){
                return true;
            }
            if(isFloat(dstType) && sval.matches("^\\d+(.\\d+)?$")){
                return true;
            }
            if(isBoolean(dstType) && sval.toLowerCase().matches("^[true|false]$")){
                return true;
            }
        }
        return isConvertible(clazz,dstType);
    }

    public static boolean isInteger(Class clazz){
        return isInTypes(clazz,int.class,Integer.class,
                short.class,Short.class,
                long.class,Long.class,
                byte.class,Byte.class,
                BigInteger.class);
    }

    public static boolean isFloat(Class clazz){
        return isInTypes(clazz,float.class,Float.class,
                double.class,Double.class,
                BigDecimal.class);
    }

    public static boolean isBoolean(Class clazz){
        return isInTypes(clazz,boolean.class,Boolean.class);
    }

    public static boolean isConvertible(Class srcType,Class dstType){
        if(srcType==null){
            return false;
        }
        if(dstType==null){
            return false;
        }
        if(Void.class.equals(dstType)){
            return false;
        }
        // Object 类型都可以转换
        if(Object.class.equals(dstType)){
            return true;
        }
        // String 类型都可以转换
        if(String.class.equals(dstType)){
            return true;
        }
        // 同类型或者派生关系都可以转换
        if(isInTypes(srcType,dstType)){
            return true;
        }
        // 整形之间都可以转换
        if(isInteger(srcType)
                &&
                isInteger(dstType)
        ){
            return true;
        }

        // 浮点数之间可转换，整形可转换为浮点类型
        if(isInTypes(srcType,float.class,Float.class,
                double.class,Double.class,
                BigDecimal.class,
                int.class,Integer.class,
                short.class,Short.class,
                long.class,Long.class,
                byte.class,Byte.class,
                BigInteger.class)
                &&
                isFloat(dstType)
        ){
            return true;
        }

        // 字符型之间可以转换
        if(isInTypes(srcType,char.class,Character.class)
            &&
                isInTypes(dstType,char.class,Character.class)
        ){
            return true;
        }

        // 时间类型之间可以转换
        if(isInTypes(srcType,java.util.Date.class,java.sql.Timestamp.class,java.sql.Date.class)
         &&
                isInTypes(srcType,java.util.Date.class,java.sql.Timestamp.class,java.sql.Date.class)
        ){
            return true;
        }

        return false;
    }

    public static Object tryConvertible(Object val,Class dstType){
        if(val==null){
            return val;
        }
        if(Object.class.equals(dstType)){
            return val;
        }
        if(String.class.equals(dstType)){
            return String.valueOf(val);
        }
        Class srcType=val.getClass();
        if(isInTypes(srcType,dstType)){
            return val;
        }
        if(val instanceof String){
            String sval=(String)val;
            if(isInteger(dstType) && sval.matches("^\\d+$")){
                val=new BigInteger(sval);
                srcType=val.getClass();
            }
            if(isFloat(dstType) && sval.matches("^\\d+(.\\d+)?$")){
                val=new BigDecimal(sval);
                srcType=val.getClass();
            }
            if(isBoolean(dstType) && sval.toLowerCase().matches("^[true|false]$")){
                if("true".equals(sval.toLowerCase())){
                    return true;
                }else if("false".equals(sval.toLowerCase())){
                    return false;
                }
            }
        }
        if(isInteger(srcType)
                &&
                isInteger(dstType)
        ){
            BigInteger ival=new BigInteger(String.valueOf(val));
            if(isInTypes(dstType,BigInteger.class)){
                return ival;
            }
            else if(isInTypes(dstType,int.class,Integer.class)){
                return ival.intValue();
            }else if(isInTypes(dstType,short.class,Short.class)){
                return ival.shortValue();
            }else if(isInTypes(dstType,long.class,Long.class)){
                return ival.longValue();
            }else if(isInTypes(dstType,byte.class,Byte.class)){
                return ival.byteValue();
            }
        }

        if(isInTypes(srcType,float.class,Float.class,
                double.class,Double.class,
                BigDecimal.class,
                int.class,Integer.class,
                short.class,Short.class,
                long.class,Long.class,
                byte.class,Byte.class,
                BigInteger.class)
                &&
                isFloat(dstType)
        ){
            BigDecimal dval=new BigDecimal(String.valueOf(val));
            if(isInTypes(dstType,BigDecimal.class)){
                return dval;
            }
            else if(isInTypes(dstType,float.class,Float.class)){
                return dval.floatValue();
            }else if(isInTypes(dstType,double.class,Double.class)){
                return dval.doubleValue();
            }
        }

        if(isInTypes(srcType,char.class,Character.class)
                &&
                isInTypes(dstType,char.class,Character.class)
        ){
            Character cval=(Character)val;
            return cval;
        }

        if(isInTypes(srcType,java.util.Date.class,java.sql.Timestamp.class,java.sql.Date.class)
                &&
                isInTypes(srcType,java.util.Date.class,java.sql.Timestamp.class,java.sql.Date.class)
        ){
            long tval=((java.util.Date)val).getTime();
            if(isInTypes(dstType,java.sql.Timestamp.class)){
                return new java.sql.Timestamp(tval);
            }else if(isInTypes(dstType,java.sql.Date.class)){
                return new java.sql.Date(tval);
            }else if(isInTypes(dstType,java.util.Date.class)){
                return new java.util.Date(tval);
            }
        }

        return val;
    }
}
