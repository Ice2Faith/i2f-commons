package i2f.commons.component.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ltb
 * @date 2022/2/25 8:48
 * @desc
 */
@Slf4j
@EnableScheduling
@Configuration
@ConfigurationProperties(prefix = "project.schedule")
public class ScheduleConfig implements SchedulingConfigurer {

    private int poolSize=10;
    private String threadNamePrefix="epcp-schedule-thread-";
    private int awaitTerminationSeconds=600;
    private boolean waitForTaskToCompleteOnShutdown=true;
    private String rejectExecutionHandler="AbortPolicy";

    @Bean(destroyMethod = "shutdown", name = "schedulerPool")
    public ThreadPoolTaskScheduler schedulerPool(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix(threadNamePrefix);
        scheduler.setAwaitTerminationSeconds(awaitTerminationSeconds);
        scheduler.setWaitForTasksToCompleteOnShutdown(waitForTaskToCompleteOnShutdown);
        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        RejectedExecutionHandler handler=new ThreadPoolExecutor.AbortPolicy();
        if("AbortPolicy".equals(rejectExecutionHandler)){
            handler=new ThreadPoolExecutor.AbortPolicy();
        }else if("CallerRunsPolicy".equals(rejectExecutionHandler)){
            handler=new ThreadPoolExecutor.DiscardOldestPolicy();
        }else if("DiscardPolicy".equals(rejectExecutionHandler)){
            handler=new ThreadPoolExecutor.DiscardPolicy();
        }else if("DiscardOldestPolicy".equals(rejectExecutionHandler)){
            handler=new ThreadPoolExecutor.DiscardOldestPolicy();
        }
        scheduler.setRejectedExecutionHandler(handler);
        return scheduler;
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.setTaskScheduler(schedulerPool());
    }

}
