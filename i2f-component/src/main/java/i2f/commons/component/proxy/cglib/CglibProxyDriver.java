package i2f.commons.component.proxy.cglib;


import i2f.commons.component.proxy.cglib.core.CglibProxy;
import i2f.commons.component.proxy.cglib.core.IInvokeInterceptor;
import i2f.commons.component.proxy.cglib.core.BaseMethodInvoker;

public class CglibProxyDriver {
    private static CglibProxy proxy = new CglibProxy();

    public static <T> T getProxy(Class<T> srcClass, BaseMethodInvoker invoker) {
        //设置DEBUG的生成class文件路径
        //System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "cglib/proxy");
        return proxy.getProxy(srcClass, invoker);
    }

    public static <T> T getProxy(Class<T> srcClass, IInvokeInterceptor interceptor) {
        return proxy.getProxy(srcClass, interceptor);
    }

    public static <T> T getLoggedProxy(Class<T> clazz) {
        return getProxy(clazz, new LoggerInterceptor());
    }

}
