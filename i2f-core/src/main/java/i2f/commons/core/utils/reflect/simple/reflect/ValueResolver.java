package i2f.commons.core.utils.reflect.simple.reflect;

/**
 * @author ltb
 * @date 2022/3/11 11:13
 * @desc
 */
public class ValueResolver {

    public static Object get(Object obj,String routePatten){
        ValueAccessor accessor=ValueVisitor.visit(obj,routePatten);
        return accessor.get();
    }

    public static void set(Object obj,String routePatten,Object val){
        ValueAccessor accessor=ValueVisitor.visit(obj,routePatten);
        accessor.set(val);
    }

}
