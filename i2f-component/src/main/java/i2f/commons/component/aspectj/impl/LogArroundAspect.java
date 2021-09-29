package i2f.commons.component.aspectj.impl;

import i2f.commons.core.utils.str.AppendUtil;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class LogArroundAspect extends BaseArroundAspect{
    protected void outLog(Object ... args){
        System.out.println(AppendUtil.str(args));
    }
    @Override
    public void before(ProceedingJoinPoint pjp, Class clazz, Method method, Object[] args) throws Throwable {
        super.before(pjp, clazz, method, args);
        Parameter[] params= method.getParameters();
        outLog("**** aspect log on:Class(",clazz.getName(),"),Method(",method.getName(),") ****");

        if(args.length>0){
            outLog("\t** begin params:");
            for(int i=0;i< args.length;i++){
                Object item=args[i];
                Parameter param=params[i];
                outLog("\t\t* ",param.getName(),"(",param.getType().getSimpleName(),"):",item);
            }
            outLog("\t** end params.");
        }
    }

    @Override
    public Object after(Object result, ProceedingJoinPoint pjp, Class clazz, Method method, Object[] args) throws Throwable {
        outLog("**** aspect log on:Class(",clazz.getName(),"),Method(",method.getName(),") ****");
        outLog("\t** return object:");
        outLog("\t\t* ",method.getReturnType().getName(),":",result);
        return super.after(result, pjp, clazz, method, args);
    }

    @Override
    public void excep(Throwable throwable, ProceedingJoinPoint pjp, Class clazz, Method method, Object[] args) throws Throwable {
        outLog("**** aspect log on:Class(",clazz.getName(),"),Method(",method.getName(),") ****");
        outLog("\t** exception occurred:");
        outLog("\t\t* ","msg:",throwable.getMessage());
        super.excep(throwable, pjp, clazz, method, args);
    }
}
