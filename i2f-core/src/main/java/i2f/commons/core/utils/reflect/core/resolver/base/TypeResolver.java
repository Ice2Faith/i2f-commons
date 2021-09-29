package i2f.commons.core.utils.reflect.core.resolver.base;


import i2f.commons.core.utils.safe.CheckUtil;

public class TypeResolver {

    private static Class[][] baseTypeMapping = {
            {byte.class, Byte.class},
            {short.class, Short.class},
            {int.class, Integer.class},
            {long.class, Long.class},
            {float.class, Float.class},
            {double.class, Double.class},
            {char.class, Character.class},
            {boolean.class, Boolean.class},
    };


    public static boolean isSameType(Class<?> t1, Class<?> t2) {
        boolean ret = false;
        if (t1 == t2) {
            ret = true;
        } else if (CheckUtil.isExNull(t1, t2)) {
            ret = false;
        } else if (t1.equals(t2)) {
            ret = true;
        } else {
            for (int i = 0; i < baseTypeMapping.length; i++) {
                if (baseTypeMapping[i][0].equals(t1) && baseTypeMapping[i][1].equals(t2)) {
                    ret = true;
                    break;
                }
                if (baseTypeMapping[i][1].equals(t1) && baseTypeMapping[i][0].equals(t2)) {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    public static boolean isTargetType(Class ckType,Class ... tarTypes){
        if(CheckUtil.isEmptyArray(tarTypes)){
            return false;
        }
        for(Class item : tarTypes){
            if(ckType.equals(item)){
                return true;
            }
        }
        return false;
    }
}
