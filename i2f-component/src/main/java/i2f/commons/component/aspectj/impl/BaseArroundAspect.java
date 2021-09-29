package i2f.commons.component.aspectj.impl;

import i2f.commons.component.aspectj.IArroundAspect;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public class BaseArroundAspect implements IArroundAspect {

    @Override
    public void before(ProceedingJoinPoint pjp, Class clazz, Method method, Object[] args) throws Throwable {

    }

    @Override
    public Object after(Object result, ProceedingJoinPoint pjp, Class clazz, Method method, Object[] args) throws Throwable {
        return result;
    }

    @Override
    public void excep(Throwable throwable, ProceedingJoinPoint pjp, Class clazz, Method method, Object[] args) throws Throwable {
        throw throwable;
    }
}
