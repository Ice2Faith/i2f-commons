package i2f.commons.component.quartz.driven.job;


import i2f.commons.component.quartz.QuartzUtil;
import i2f.commons.core.utils.reflect.core.resolver.base.ClassResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.MethodResolver;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class QuartzAnnotationJob implements Job {
    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Method method=(Method) QuartzUtil.getJobData(jobExecutionContext,"method");
        if(Modifier.isStatic(method.getModifiers())){
            MethodResolver.invokeStaticMethod(method.getDeclaringClass(),method.getName());
        }else{
            Class clazz=method.getDeclaringClass();
            Object obj= ClassResolver.instance(clazz);
            MethodResolver.invoke(clazz,obj,method.getName());
        }
    }
}
