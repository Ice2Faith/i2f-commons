package i2f.commons.core.utils.cache;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author ltb
 * @date 2022/3/7 15:11
 * @desc
 */
public class MemCache {
    public static volatile ConcurrentHashMap<String,Object> cacheData=new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, Long> cacheExpire=new ConcurrentHashMap<>();
    public static class MemCacheStorageItem implements Serializable{
        private static final long serialVersionUID = -1L;
        private String key;
        private Long expire;
        private Object val;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Long getExpire() {
            return expire;
        }

        public void setExpire(Long expire) {
            this.expire = expire;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }
    }

    private static volatile ScheduledExecutorService pool;
    static {
        pool=Executors.newSingleThreadScheduledExecutor();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if(pool!=null){
                    if(!pool.isShutdown()){
                        pool.shutdown();
                    }
                }
                pool=null;
            }
        }));
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ConcurrentSkipListSet<String> keys=new ConcurrentSkipListSet<>();
                for(Map.Entry<String,Long> item : cacheExpire.entrySet()){
                    long expire=item.getValue();
                    if(expire<System.currentTimeMillis()){
                        String key=item.getKey();
                        keys.add(key);
                    }
                }
                for(String item : keys){
                    cacheData.remove(item);
                    cacheExpire.remove(item);
                }
            }
        },0,3,TimeUnit.SECONDS);
    }

    public static void save2Stream(OutputStream os) throws IOException {
        ObjectOutputStream oos=new ObjectOutputStream(os);
        Iterator<String> it=cacheData.keySet().iterator();
        while(it.hasNext()){
            String item = it.next();
            if(exists(item)){
                MemCacheStorageItem sit=new MemCacheStorageItem();
                sit.setKey(item);
                sit.setExpire(cacheExpire.get(item));
                sit.setVal(cacheData.get(item));
                oos.writeObject(os);
            }
        }
    }

    public static void load4Stream(InputStream is) throws IOException {
        ObjectInputStream ois=new ObjectInputStream(is);
        while(true){
            try{
                MemCacheStorageItem sit=(MemCacheStorageItem)ois.readObject();
                cacheData.put(sit.getKey(),sit.getVal());
                if(sit.getExpire()!=null){
                    cacheExpire.put(sit.getKey(),sit.getExpire());
                }
            }catch(Exception e){
                e.printStackTrace();
                break;
            }
        }
    }

    public static boolean exists(String key){
        if(cacheExpire.containsKey(key)){
            Long arriveTime=cacheExpire.get(key);
            long time=System.currentTimeMillis();
            if(time>arriveTime){
                cacheExpire.remove(key);
                return false;
            }else{
                return true;
            }
        }else{
            return cacheData.containsKey(key);
        }
    }
    public Set<String> keys(String regex){
        Set<String> ret=new HashSet<>();
        Iterator<String> it=cacheData.keySet().iterator();
        while(it.hasNext()){
            String item = it.next();
            if(exists(item)){
                ret.add(item);
            }
        }
        return ret;
    }
    public boolean expire(String key,long expire,TimeUnit expireUnit){
        if(exists(key)){
            long milli=expireUnit.toMillis(expire);
            cacheExpire.put(key,System.currentTimeMillis()+milli);
            return true;
        }else{
            return false;
        }
    }
    public static void set(String key,Object val){
        cacheData.put(key, val);
    }
    public static void set(String key, long expire, TimeUnit expireUnit,Object val){
        long milli=expireUnit.toMillis(expire);
        cacheData.put(key,val);
        cacheExpire.put(key,System.currentTimeMillis()+milli);
    }
    public static void setSecond(String key,long expire,Object val){
        set(key,expire,TimeUnit.SECONDS,val);
    }
    public static void setMinutes(String key,long expire,Object val){
        set(key,expire,TimeUnit.MINUTES,val);
    }
    public static void setHours(String key,long expire,Object val){
        set(key,expire,TimeUnit.HOURS,val);
    }
    public static void setDays(String key,long expire,Object val){
        set(key,expire,TimeUnit.DAYS,val);
    }
    public static Object get(String key){
        if(exists(key)){
            return cacheData.get(key);
        }
        return null;
    }

    public static<T> T getAs(String key){
        if(exists(key)){
            return (T)cacheData.get(key);
        }
        return null;
    }

}
