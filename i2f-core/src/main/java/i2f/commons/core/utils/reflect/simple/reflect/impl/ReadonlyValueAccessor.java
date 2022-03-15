package i2f.commons.core.utils.reflect.simple.reflect.impl;


import i2f.commons.core.utils.reflect.simple.reflect.ValueAccessor;
import i2f.commons.core.utils.reflect.simple.reflect.exception.ReadOnlyAccessException;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
public class ReadonlyValueAccessor implements ValueAccessor {
    public Object obj;
    public ReadonlyValueAccessor(Object obj) {
        this.obj=obj;
    }

    @Override
    public Object get() {
        return obj;
    }

    @Override
    public void set(Object obj) {
        throw new ReadOnlyAccessException("object is readonly cannot be set.");
    }
}
