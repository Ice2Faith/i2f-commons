package i2f.commons.core.utils.thread.std;


import i2f.commons.core.utils.thread.ThreadUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CachedFixedThreadPool {
    public static volatile int POOL_SIZE=10;
    protected static volatile ExecutorService pool;
    public static ExecutorService getPool(){
        if(pool==null){
            synchronized (CachedFixedThreadPool.class){
                if(pool==null){
                    pool= ThreadUtil.cachedFixedPool(POOL_SIZE);
                }
            }
        }
        return pool;
    }
    public static Future<?> submit(Runnable runnable){
        return getPool().submit(runnable);
    }
    public static<T> Future<T> submit(Callable<T> callable){
        return getPool().submit(callable);
    }
}
