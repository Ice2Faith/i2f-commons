package i2f.commons.component.cglib.core;

import java.lang.reflect.Method;

public interface IInvokeInterceptor {
    /**
     * 在真正调用之前调用，
     * 可以对入参、注解等进行其他处理
     * 并且，你可以返回结果，
     * 如果返回值为null,则调用真实函数
     * 如果返回值不为null,则提前返回，不再调用真实函数
     *
     * @param obj    函数所在的对象
     * @param method 函数对象
     * @param args   函数的调用参数
     * @return
     */
    Object beforeIvk(Object obj, Method method, Object[] args);

    /**
     * 在真正调用完毕之后调用
     *
     * @param retVal 真实调用的结果
     * @param obj    调用所在的函数
     * @param method 调用的方法
     * @param args   调用的参数 -- 注意，这个参数如果你在beforeInvoke中修改了，这将是修改的值
     * @return
     */
    Object afterIvk(Object retVal, Object obj, Method method, Object[] args);
}
