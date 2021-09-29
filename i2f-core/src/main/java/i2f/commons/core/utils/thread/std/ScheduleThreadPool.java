package i2f.commons.core.utils.thread.std;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleThreadPool {
    public static volatile int POOL_SIZE=10;
    protected static volatile ScheduledExecutorService pool;

    public static ScheduledExecutorService getPool(){
        if(pool==null){
            synchronized (ScheduleThreadPool.class){
                if(pool==null){
                    pool= Executors.newScheduledThreadPool(POOL_SIZE);
                }
            }
        }
        return pool;
    }

    /**
     * 指定间隔启动
     * @param runnable 任务
     * @param initDelay 初始化延迟
     * @param intrevalTime 间隔时长
     * @param unit 时长单位
     * @return
     */
    public static ScheduledFuture<?> intreval(Runnable runnable, long initDelay, long intrevalTime, TimeUnit unit){
        return getPool().scheduleAtFixedRate(runnable, initDelay, intrevalTime, unit);
    }

    /**
     * 在上一次完成后间隔时间启动
     * @param runnable 任务
     * @param initDelay 初始化延迟
     * @param delayTime 间隔时长
     * @param unit 时长单位
     * @return
     */
    public static ScheduledFuture<?> delay(Runnable runnable,long initDelay,long delayTime,TimeUnit unit){
        return getPool().scheduleWithFixedDelay(runnable, initDelay, delayTime, unit);
    }
    public static ScheduledFuture<?> intreval(Runnable runnable,long intrevalTime,TimeUnit unit){
        return getPool().scheduleAtFixedRate(runnable,0L,intrevalTime,unit);
    }
    public static ScheduledFuture<?> intreval(Runnable runnable,long intrevalTimeMilliSecond){
        return getPool().scheduleAtFixedRate(runnable,0L,intrevalTimeMilliSecond,TimeUnit.MILLISECONDS);
    }
    public static ScheduledFuture<?> delay(Runnable runnable,long delayTime,TimeUnit unit){
        return getPool().scheduleWithFixedDelay(runnable,0L,delayTime,unit);
    }
    public static ScheduledFuture<?> delay(Runnable runnable,long delayTimeMillSecond){
        return getPool().scheduleWithFixedDelay(runnable,0L,delayTimeMillSecond,TimeUnit.MILLISECONDS);
    }
}
