package i2f.commons.component.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzUtil {
    public static SchedulerFactory getStdSchedulerFactory() {
        return new StdSchedulerFactory();
    }

    public static Scheduler getScheduler() throws SchedulerException {
        return getScheduler(getStdSchedulerFactory());
    }

    public static Scheduler getScheduler(SchedulerFactory factory) throws SchedulerException {
        return factory.getScheduler();
    }
    public static JobDetail getJobDetail(Class<? extends Job> job,
                                         String id, String group){
        return getJobDetail(job, id, group,null);
    }
    public static JobDetail getJobDetail(Class<? extends Job> job,
                                         String id, String group,
                                         JobDataMap datas) {
        JobBuilder builder=JobBuilder.newJob(job)
                .withIdentity(id, group);
        if(datas!=null){
            builder.usingJobData(datas);
        }
        return builder.build();
    }
    public static Trigger getIntervalTrigger(String id,
                                             int milliSecond){
        return getIntervalTrigger(id,milliSecond,-1,null);
    }
    public static Trigger getIntervalTrigger(String id,
                                             long milliSecond, int count){
        return getIntervalTrigger(id,milliSecond,count,null);
    }
    public static Trigger getIntervalTrigger(String id,
                                             long milliSecond, int count,
                                             JobDataMap datas) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(milliSecond);
        if (count >= 0) {
            builder.withRepeatCount(count);
        } else {
            builder.repeatForever();
        }
        TriggerBuilder triggerBuilder=TriggerBuilder.newTrigger()
                .withIdentity(id)
                .startNow()
                .withSchedule(builder);
        if(datas!=null){
            triggerBuilder.usingJobData(datas);
        }
        return triggerBuilder.build();
    }

    public static Trigger getCronTrigger(String id,String cronExp){
        return getCronTrigger(id,cronExp,null);
    }
    //线生成Cron表达式的工具：http://cron.qqe2.com/
    public static Trigger getCronTrigger(String id,
                                             String cronExp,
                                             JobDataMap datas){
        CronScheduleBuilder builder=CronScheduleBuilder.cronSchedule(cronExp);
        TriggerBuilder triggerBuilder=TriggerBuilder.newTrigger()
                .withIdentity(id)
                .startNow()
                .withSchedule(builder);
        if(datas!=null){
            triggerBuilder.usingJobData(datas);
        }
        return triggerBuilder.build();
    }

    public static Scheduler doSchedule(JobDetail jobDetail,Trigger trigger) throws SchedulerException {
        Scheduler scheduler=getScheduler();
        doSchedule(scheduler,jobDetail,trigger);
        return scheduler;
    }

    public static void doSchedule(Scheduler scheduler,JobDetail jobDetail,Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

    public static Object getJobData(JobExecutionContext context,Object key){
        return context.getJobDetail().getJobDataMap().get(key);
    }
    public static Object getTriggerData(JobExecutionContext context,Object key){
        return context.getTrigger().getJobDataMap().get(key);
    }
}
