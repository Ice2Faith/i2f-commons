package i2f.commons.core.data.interfaces.impl;

import i2f.commons.core.data.interfaces.IExecute;

import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class ThreadTaskExecutor implements IExecute<Runnable> {
    private ExecutorService pool;
    public ThreadTaskExecutor(){

    }
    public ThreadTaskExecutor(ExecutorService service){
        this.pool=service;
    }
    @Override
    public void exec(Runnable val) {
        if(this.pool!=null){
            pool.submit(val);
        }else{
            new Thread(val).start();
        }
    }
}
