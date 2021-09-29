package i2f.commons.component.spring.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2021/8/11
 */
@Aspect
@Component
public class AspectLog {
    @Pointcut("execution(public * com..*.*(..))")
    public void comPkgAnyWhereAspect(){}

    @Around("comPkgAnyWhereAspect()")
    public Object comPkgAnyWhereLog(ProceedingJoinPoint pjp) throws Throwable{
        return AopLogDriver.logAround(pjp);
    }

}
