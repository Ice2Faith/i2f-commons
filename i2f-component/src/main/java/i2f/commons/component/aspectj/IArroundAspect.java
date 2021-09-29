package i2f.commons.component.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public interface IArroundAspect {
    void before(ProceedingJoinPoint pjp, Class clazz, Method method,Object[] args) throws Throwable;
    Object after(Object result,ProceedingJoinPoint pjp, Class clazz, Method method,Object[] args) throws Throwable;
    void excep(Throwable throwable, ProceedingJoinPoint pjp, Class clazz, Method method, Object[] args) throws Throwable;
}
