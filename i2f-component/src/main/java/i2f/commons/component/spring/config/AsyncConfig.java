package i2f.commons.component.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ltb
 * @date 2022/2/25 9:24
 * @desc
 */
@Slf4j
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "project.async")
public class AsyncConfig implements AsyncConfigurer {
    private int maxPoolSize=100;
    private int corePoolSize=10;
    private int queueCapital=20;
    private String threadNamePrefix="epcp-async-thread-";
    private int keepAliveSeconds=60;
    private String rejectExecutionHandler="AbortPolicy";


    @Bean
    public ThreadPoolTaskExecutor executorPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //核心线程数
        executor.setCorePoolSize(corePoolSize);
        //任务队列的大小
        executor.setQueueCapacity(queueCapital);
        //线程前缀名
        executor.setThreadNamePrefix(threadNamePrefix);
        //线程存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);

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
        executor.setRejectedExecutionHandler(handler);
        //线程初始化
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return executorPool();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("异步任务执行出现异常, message {}, emthod {}, params {}", throwable, method, objects);
        };
    }
}
