package i2f.commons.component.proxy.cglib.core;

import i2f.commons.component.proxy.cglib.core.impl.NoneEnhancerSetting;
import net.sf.cglib.proxy.Enhancer;

public class CglibProxy {
    private IEnhancerSetting setting;

    public CglibProxy() {
        this.setting = new NoneEnhancerSetting();
    }

    public CglibProxy(IEnhancerSetting setting) {
        this.setting = setting;
    }

    public IEnhancerSetting setSetting(IEnhancerSetting setting) {
        IEnhancerSetting old = this.setting;
        this.setting = setting;
        return old;
    }

    public <T> T getProxy(Class<T> srcClass, BaseMethodInvoker invoker) {
        Enhancer enhancer = new Enhancer();
        if (setting != null) {
            setting.set(enhancer);
        }
        enhancer.setUseFactory(true);
        enhancer.setSuperclass(srcClass);
        enhancer.setCallback(invoker);
        return (T) enhancer.create();
    }

    public <T> T getProxy(Class<T> srcClass, IInvokeInterceptor interceptor) {
        return getProxy(srcClass, new BaseMethodInvoker(interceptor));
    }
}
