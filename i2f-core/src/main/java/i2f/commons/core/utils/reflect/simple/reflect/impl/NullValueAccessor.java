package i2f.commons.core.utils.reflect.simple.reflect.impl;

import i2f.reflect.ValueAccessor;
import i2f.reflect.exception.NullAccessException;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
public class NullValueAccessor implements ValueAccessor {
    @Override
    public Object get() {
        return null;
    }

    @Override
    public void set(Object obj) {
        throw new NullAccessException("object is null cannot be set.");
    }
}
