package i2f.commons.core.utils.reflect.simple.reflect;

/**
 * @author ltb
 * @date 2022/3/17 8:42
 * @desc
 */
public interface PropertyAccessor extends ValueAccessor{
    void setInvokeObject(Object ivkObj);
    String getName();
    boolean writable();
    boolean readable();
    Class getType();
}
