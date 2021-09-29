package i2f.commons.component.spring.aop;

import i2f.commons.core.utils.str.AppendUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class AopLogUtil {
    public static Object controllerLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms=(MethodSignature) joinPoint.getSignature();
        Method method=ms.getMethod();
        Class clazz=method.getDeclaringClass();

        Parameter[] params=method.getParameters();
        Object[] args=joinPoint.getArgs();
        RequestMapping ann=method.getAnnotation(RequestMapping.class);
        PostMapping annp=method.getAnnotation(PostMapping.class);
        GetMapping anng=method.getAnnotation(GetMapping.class);

        String url="";
        if(ann!=null){
            url="rmap:"+ann.value()[0];
        }else if(annp!=null){
            url="post:"+annp.value()[0];
        }else if(anng!=null){
            url="get:"+anng.value()[0];
        }

        AppendUtil.AppendBuilder buffer= AppendUtil.buffer()
        .addsLine("$$-aop-ivk-controller:")
        .addsLine("\tmap url:",url)
        .addsLine("\tin class:",clazz.getSimpleName())
        .addsLine("\tivk method:",method.getName());
        if(args.length>0){
            buffer.addsLine("\twith params:");
            for(int i=0;i< args.length;i++){
                Object item=args[i];
                Parameter param=params[i];
                buffer.addsLine("\t\t",param.getName(),"(",param.getType().getSimpleName(),"):",item);
            }
        }
        System.out.println(buffer.done());
        Object rs=joinPoint.proceed(args);

        System.out.println("\t$$-aop-controller-result:"+(rs==null?method.getReturnType().getSimpleName():rs.getClass().getSimpleName())+":"+rs);

        return rs;
    }

    public static Object serviceLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms=(MethodSignature) joinPoint.getSignature();
        Method method=ms.getMethod();
        Class clazz=method.getDeclaringClass();

        Parameter[] params=method.getParameters();
        Object[] args=joinPoint.getArgs();

        AppendUtil.AppendBuilder buffer= AppendUtil.buffer()
        .addsLine("$$-aop-ivk-service:")
        .addsLine("\tin class:",clazz.getSimpleName())
        .addsLine("\tivk method:",method.getName());
        if(args.length>0){
            buffer.addsLine("\twith params:");
            for(int i=0;i< args.length;i++){
                Object item=args[i];
                Parameter param=params[i];
                buffer.addsLine("\t\t",param.getName(),"(",param.getType().getSimpleName(),"):",item);
            }
        }
        System.out.println(buffer.done());
        Object rs=joinPoint.proceed(args);

        System.out.println("\t$$-aop-service-result:"+(rs==null?method.getReturnType().getSimpleName():rs.getClass().getSimpleName())+":"+rs);

        return rs;
    }

    public static Object daoLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms=(MethodSignature) joinPoint.getSignature();
        Method method=ms.getMethod();
        Class clazz=method.getDeclaringClass();

        Parameter[] params=method.getParameters();
        Object[] args=joinPoint.getArgs();

        AppendUtil.AppendBuilder buffer= AppendUtil.buffer()
        .addsLine("$$-aop-ivk-dao:")
        .addsLine("\tin class:",clazz.getSimpleName())
        .addsLine("\tivk method:",method.getName());
        if(args.length>0){
            buffer.addsLine("\twith params:");
            for(int i=0;i< args.length;i++){
                Object item=args[i];
                Parameter param=params[i];
                buffer.addsLine("\t\t",param.getName(),"(",param.getType().getSimpleName(),"):",item);
            }
        }

        System.out.println(buffer.done());

        Object rs=joinPoint.proceed(args);

        System.out.println("\t$$-aop-dao-result:"+(rs==null?method.getReturnType().getSimpleName():rs.getClass().getSimpleName())+":"+rs);

        return rs;
    }
}
