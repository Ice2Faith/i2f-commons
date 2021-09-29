package i2f.commons.component.aspectj.impl;

import i2f.commons.core.validator.ValidateDriver;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public class ValidateArroundAspect extends BaseArroundAspect{
    @Override
    public void before(ProceedingJoinPoint pjp, Class clazz, Method method, Object[] args) throws Throwable {
        ValidateDriver.test(method, args);
    }
}
