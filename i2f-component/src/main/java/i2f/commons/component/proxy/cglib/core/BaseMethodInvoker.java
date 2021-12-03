package i2f.commons.component.proxy.cglib.core;

import i2f.commons.component.proxy.cglib.core.impl.NoneInterceptor;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class BaseMethodInvoker implements MethodInterceptor {
    private IInvokeInterceptor interceptor;

    public BaseMethodInvoker() {
        interceptor = new NoneInterceptor();
    }

    public BaseMethodInvoker(IInvokeInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public IInvokeInterceptor setInterceptor(IInvokeInterceptor interceptor) {
        IInvokeInterceptor old = this.interceptor;
        this.interceptor = interceptor;
        return old;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (interceptor != null) {
            Object beforeRetVal = interceptor.beforeIvk(obj, method, args);
            if (beforeRetVal != null) {
                return beforeRetVal;
            }
        }

        Object ret = methodProxy.invokeSuper(obj, args);
        if (interceptor != null) {
            ret = interceptor.afterIvk(ret, obj, method, args);
        }

        return ret;
    }
}
