package i2f.commons.component.spring.log;

import i2f.commons.component.spring.log.annotation.RequireLog;
import i2f.commons.core.utils.str.AppendUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ltb
 * @date 2021/8/11
 */
public class AopLogDriver {

    public static volatile boolean OPEN_LOG=true;

    public static Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms=(MethodSignature) joinPoint.getSignature();
        Method method=ms.getMethod();
        Class clazz=method.getDeclaringClass();

        Parameter[] params=method.getParameters();
        Object[] args=joinPoint.getArgs();

        if(!OPEN_LOG){
            return joinPoint.proceed(args);
        }

        RequireLog log=method.getAnnotation(RequireLog.class);
        if(log==null){
            log=(RequireLog)clazz.getAnnotation(RequireLog.class);
        }
        if(log==null){
            return joinPoint.proceed(args);
        }

        Logger logger= LoggerFactory.getLogger(clazz.getName()+"->"+method.getName());
        if(log.inParams()){
            AppendUtil.AppendBuilder buffer= AppendUtil.buffer();
            addHttpRelativeInfo(buffer,clazz,method,params,args);
            if(args.length>0){
                buffer.addsLine("\tparams:");
                for(int i=0;i< args.length;i++){
                    Object item=args[i];
                    Parameter param=params[i];
                    buffer.addsLine("\t\t",param.getName(),"(",param.getType().getSimpleName(),"):",item);
                }
            }
            logger.info(buffer.done());
        }

        Object rs=joinPoint.proceed(args);

        if(log.outResult()){
            logger.info("\tresult:"+(rs==null?method.getReturnType().getSimpleName():rs.getClass().getSimpleName())+":"+rs);
        }

        return rs;
    }

    private static void addHttpRelativeInfo(AppendUtil.AppendBuilder buffer, Class clazz, Method method, Parameter[] params, Object[] args) {
        Controller controller=(Controller) clazz.getAnnotation(Controller.class);
        RestController restController=(RestController) clazz.getAnnotation(RestController.class);
        if(controller!=null || restController!=null){
            RequestMapping baseMapping=(RequestMapping)clazz.getAnnotation(RequestMapping.class);
            String baseUrl="";
            if(baseMapping!=null){
                baseUrl=baseMapping.path()[0];
            }
            RequestMapping ann=method.getAnnotation(RequestMapping.class);
            PostMapping annp=method.getAnnotation(PostMapping.class);
            GetMapping anng=method.getAnnotation(GetMapping.class);
            DeleteMapping annd=method.getAnnotation(DeleteMapping.class);
            PutMapping annpt=method.getAnnotation(PutMapping.class);

            String type="";
            String url=null;
            if(ann!=null){
                type="all";
                url=ann.path()[0];
            }else if(annp!=null){
                type="post";
                url=annp.path()[0];
            }else if(anng!=null){
                type="get";
                url=anng.path()[0];
            }else if(annd!=null){
                type="delete";
                url=annd.path()[0];
            }else if(annpt!=null){
                type="put";
                url=annpt.path()[0];
            }
            if(url!=null){
                buffer.addsLine("\t",type," reqUrl:",baseUrl,"/",url);
            }

        }
    }
}
