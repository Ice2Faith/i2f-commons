package i2f.commons.component.aspectj;

import i2f.commons.core.utils.safe.CheckUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class AspectUtil {

    public static Object proxyAround(ProceedingJoinPoint pjp,IArroundAspect arroundAspect) throws Throwable {
        if(CheckUtil.isNull(arroundAspect)){
            return pjp.proceed();
        }
        MethodSignature ms=(MethodSignature) pjp.getSignature();
        Method method=ms.getMethod();
        Class clazz=method.getDeclaringClass();
        Parameter[] params=method.getParameters();
        Object[] args=pjp.getArgs();

        arroundAspect.before(pjp,clazz,method,args);

        Object result=null;
        try{
            result=pjp.proceed(args);
        }catch(Throwable e){
            arroundAspect.excep(e,pjp,clazz,method,args);
        }

        result=arroundAspect.after(result,pjp,clazz,method,args);

        return result;
    }

}
