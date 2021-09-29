package i2f.commons.core.utils.thread;

import java.util.concurrent.*;

public class ThreadUtil {

    public static boolean sleep(long millSecond){
        try {
            Thread.sleep(millSecond);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean sleep(long time,TimeUnit unit){
        long t=TimeUnit.MILLISECONDS.convert(time,unit);
        return sleep(t);
    }

    public static ExecutorService fixedPool(int poolSize){
        ExecutorService service=Executors.newFixedThreadPool(poolSize);
        return service;
    }

    public static ScheduledExecutorService scheduledPool(int poolSize){
        ScheduledExecutorService service=Executors.newScheduledThreadPool(poolSize);
        return service;
    }

    public static ExecutorService cachedPool(){
        ExecutorService service=Executors.newCachedThreadPool();
        return service;
    }

    public static ExecutorService cachedFixedPool(int corePoolSize){
        return new ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public static ScheduledFuture<?> scheduleFixedRate(ScheduledExecutorService pool, Runnable runnable, long initDelay, long rate, TimeUnit unit){
        return pool.scheduleAtFixedRate(runnable,initDelay,rate,unit);
    }

    public static ScheduledFuture<?> scheduleFixedDelay(ScheduledExecutorService pool,Runnable runnable,long initDelay, long delay, TimeUnit unit){
        return pool.scheduleWithFixedDelay(runnable,initDelay,delay,unit);
    }

    public static void toDo(Runnable task){
        new Thread(task).start();
    }

    public static<T> FutureTask<T> toDo(Callable<T> task){
        FutureTask<T> future=new FutureTask<T>(task);
        new Thread(future).start();
        return future;
    }

    public static void toDo(Runnable task,String name){
        new Thread(task,name).start();
    }

    public static<T> FutureTask<T> toDo(Callable<T> task,String name){
        FutureTask<T> future=new FutureTask<T>(task);
        new Thread(future,name).start();
        return future;
    }

    public static<T> T result(FutureTask<T> future,T defVal) {
        try {
            //将会同步等待结果返回，或是异常抛出
            return future.get();
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
        return defVal;
    }

}
