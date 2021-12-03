package i2f.commons.component.proxy.cglib.core.impl;


import i2f.commons.component.proxy.cglib.core.IInvokeInterceptor;

import java.lang.reflect.Method;

public class NoneInterceptor implements IInvokeInterceptor {
    @Override
    public Object beforeIvk(Object obj, Method method, Object[] args) {
        return null;
    }

    @Override
    public Object afterIvk(Object retVal, Object obj, Method method, Object[] args) {
        return retVal;
    }
}
