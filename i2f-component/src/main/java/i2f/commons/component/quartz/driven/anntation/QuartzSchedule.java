package i2f.commons.component.quartz.driven.anntation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzSchedule {
    public enum ScheduleType{Interval,Cron};
    ScheduleType type() default ScheduleType.Interval;
    long intervalMillSecond() default 1000;
    int intervalCount() default -1;
    String cron() default "* * * * * ? *";

    String id() ;
    String group() ;
}
