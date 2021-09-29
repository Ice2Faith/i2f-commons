package i2f.commons.component.quartz.driven;


import i2f.commons.component.quartz.QuartzUtil;
import i2f.commons.component.quartz.driven.anntation.QuartzSchedule;
import i2f.commons.component.quartz.driven.job.QuartzAnnotationJob;
import i2f.commons.core.utils.pkg.PackageScanner;
import i2f.commons.core.utils.pkg.data.ClassMetaData;
import i2f.commons.core.utils.reflect.core.resolver.base.AnnotationResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.MethodResolver;
import org.quartz.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuartzScanner {
    public static List<Scheduler> scanBasePackage(String ... pkgs) throws IOException {
        List<ClassMetaData> classNames= PackageScanner.scanClasses(null,pkgs);
        List<Class> classes=new ArrayList<>();
        for(ClassMetaData item : classNames){
            classes.add(item.getClazz());
        }
        return scans(classes);
    }
    public static List<Scheduler> scans(List<Class> classes){
        List<Scheduler> ret=new ArrayList<>();
        for(Class item : classes){
            List<Scheduler> list=scan(item);
            ret.addAll(list);
        }
        return ret;
    }
    public static List<Scheduler> scans(Class ... clazzes){
        List<Scheduler> ret=new ArrayList<>();
        for(Class item : clazzes){
            List<Scheduler> list=scan(item);
            ret.addAll(list);
        }
        return ret;
    }
    public static List<Scheduler> scan(Class clazz){
        List<Scheduler> ret=new ArrayList<>();
        Set<Method> methods= MethodResolver.getAllMethodsWithAnnotations(clazz, QuartzSchedule.class);
        if(methods.size()==0){
            return ret;
        }
        for(Method item : methods){
            QuartzSchedule ann= AnnotationResolver.getMethodAnnotation(item,false,false,true,QuartzSchedule.class);
            if(ann==null){
                continue;
            }
            try{
                Scheduler scheduler=makeSchedule(item,ann);
                if(scheduler==null){
                    continue;
                }
                ret.add(scheduler);
            }catch(Exception e){

            }
        }
        return ret;
    }

    private static Scheduler makeSchedule(Method method, QuartzSchedule ann) throws SchedulerException {
        if(method==null || ann==null){
            return null;
        }
        JobDataMap dataMap=new JobDataMap();
        dataMap.put("method",method);
        JobDetail jobDetail= QuartzUtil.getJobDetail(QuartzAnnotationJob.class,ann.id(), ann.group(),dataMap);
        Trigger trigger=null;
        if(ann.type()== QuartzSchedule.ScheduleType.Interval){
            trigger=QuartzUtil.getIntervalTrigger(ann.id(),ann.intervalMillSecond(),ann.intervalCount());
        }else if(ann.type()== QuartzSchedule.ScheduleType.Cron){
            trigger=QuartzUtil.getCronTrigger(ann.id(),ann.cron());
        }
        Scheduler scheduler=QuartzUtil.doSchedule(jobDetail,trigger);
        return scheduler;
    }
}
