package i2f.commons.core.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2021/8/12
 */
public class ThreadPoolUtil {
    public static int DEFAULT_SCHEDULE_POOL_SIZE=36;
    private static ScheduledExecutorService defaultSchedulePool;
    public static int DEFAULT_CACHE_POOL_SIZE=36;
    private static ExecutorService defaultCachePool;
    private ThreadPoolUtil(){

    }
    public static ScheduledExecutorService getDefaultSchedulePool(){
        if(defaultSchedulePool ==null){
            synchronized (ThreadUtil.class){
                if(defaultSchedulePool ==null){
                    defaultSchedulePool = ThreadUtil.scheduledPool(DEFAULT_SCHEDULE_POOL_SIZE);
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            shutdownSchedulePool();
                        }
                    }));
                }
            }
        }
        return defaultSchedulePool;
    }

    public static ExecutorService getDefaultCachePool(){
        if(defaultCachePool ==null){
            synchronized (ThreadUtil.class){
                if(defaultCachePool ==null){
                    defaultCachePool = ThreadUtil.cachedFixedPool(DEFAULT_CACHE_POOL_SIZE);
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            shutdownCachePool();
                        }
                    }));
                }
            }
        }
        return defaultCachePool;
    }

    public static void run(Runnable task){
        getDefaultCachePool().submit(task);
    }

    public static void runScheduleFixRate(Runnable task,  long rate, TimeUnit unit){
        runScheduleFixRate(task,0L,rate,unit);
    }
    public static void runScheduleFixRate(Runnable task, long initDelay, long rate, TimeUnit unit){
        getDefaultSchedulePool().scheduleAtFixedRate(task,initDelay,rate,unit);
    }

    public static void runScheduleFixDelay(Runnable task, long delay, TimeUnit unit){
        runScheduleFixDelay(task,0L,delay,unit);
    }
    public static void runScheduleFixDelay(Runnable task, long initDelay, long delay, TimeUnit unit){
        getDefaultSchedulePool().scheduleWithFixedDelay(task,initDelay,delay,unit);
    }

    public static void shutdownCachePool(){
        if(defaultCachePool!=null && !defaultCachePool.isShutdown()){
            defaultCachePool.shutdown();
            defaultCachePool=null;
        }
    }
    public static void shutdownSchedulePool(){
        if(defaultSchedulePool!=null && !defaultSchedulePool.isShutdown()){
            defaultSchedulePool.shutdown();
            defaultSchedulePool=null;
        }
    }
    public static void shutdownAllPool(){
        shutdownCachePool();
        shutdownSchedulePool();
    }
}
