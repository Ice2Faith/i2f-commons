package i2f.commons.core.utils.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/3/8 14:32
 * @desc
 */
public abstract class LatchRunnable implements Runnable{
    private CountDownLatch latch;
    public LatchRunnable(CountDownLatch latch){
        this.latch=latch;
    }
    @Override
    final public void run() {
        try{
            doTask();
        }finally {
            if(latch!=null){
                latch.countDown();
            }
        }
    }

    public abstract void doTask();

    public static void doThreadsTask(ExecutorService pool, Runnable ... runs) throws InterruptedException {
        CountDownLatch latch=new CountDownLatch(runs.length);
        for(Runnable item : runs){
            LatchRunnable task=new LatchRunnable(latch) {
                @Override
                public void doTask() {
                    item.run();
                }
            };
            if(pool!=null){
                pool.submit(task);
            }else{
                new Thread(task).start();
            }

        }
        latch.await();
    }

}
