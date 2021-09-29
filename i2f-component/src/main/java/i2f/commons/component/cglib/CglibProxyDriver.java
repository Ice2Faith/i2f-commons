package i2f.commons.component.cglib;


import i2f.commons.component.cglib.core.BaseMethodInvoker;
import i2f.commons.component.cglib.core.CglibProxy;
import i2f.commons.component.cglib.core.IInvokeInterceptor;

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
