package i2f.log.api.impl;

import i2f.log.Environment;
import i2f.log.api.LogWriter;
import i2f.log.model.BaseLogModel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/2 16:23
 * @desc
 */
public class MulticastLogWriter implements LogWriter{
    private ConcurrentHashMap<String,LogWriter> writers=new ConcurrentHashMap<>();

    @Override
    public void write(BaseLogModel log) {
        for(LogWriter item : writers.values()){
            try{
                item.write(log);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void registerLogWriter(LogWriter writer){
        String key=writer.getClass().getSimpleName();
        key=key.substring(0,1).toLowerCase()+key.substring(1);
        registerLogWriter(key,writer);
    }
    public void registerLogWriter(String key,LogWriter writer){
        if(writers.containsKey(key)){
            String msg="writer["+key+"] has been registered before";
            if(Environment.registerUniqueWriter){
                throw new RuntimeException(msg);
            }else{
                System.out.println("WARN:"+msg);
            }
        }
        writers.put(key,writer);
    }
    public void unregisterLogWriter(String key){
        if(writers.containsKey(key)){
            writers.remove(key);
        }
    }
}
