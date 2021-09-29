package i2f.commons.core.data;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruMap<K,V> extends LinkedHashMap<K,V> {
    public static enum Strategy{
        MAX_USED_MEMORY(0),MAX_COUNT(1);
        private int code;
        private Strategy(int code){
            this.code=code;
        }
        public int getCode(){
            return this.code;
        }
    }
    private double maxUsedMemoryPercent=0.85;
    private int maxCacheCount=2048;
    private Strategy strategy= Strategy.MAX_USED_MEMORY;
    public LruMap(Strategy strategy){
        this.strategy=strategy;
    }
    public LruMap(Strategy strategy,int initCapital){
        super(initCapital);
        this.strategy=strategy;
    }
    public static<K,V> LruMap<K,V> makeMaxUsedMemoryLruMap(double percent){
        LruMap<K,V> map=new LruMap<>(Strategy.MAX_USED_MEMORY);
        map.maxUsedMemoryPercent=percent;
        return map;
    }
    public static<K,V> LruMap<K,V> makeMaxCacheCountLruMap(int count){
        LruMap<K,V> map=new LruMap<>(Strategy.MAX_COUNT,count);
        map.maxCacheCount=count;
        return map;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        //在何时移除最近最久未使用
        if(strategy== Strategy.MAX_COUNT){
            if(this.size()>maxCacheCount){
                return true;
            }
        }else if(strategy== Strategy.MAX_USED_MEMORY){
            Runtime runtime=Runtime.getRuntime();
            double rate=(runtime.totalMemory()- runtime.freeMemory()*1d)/ runtime.totalMemory();
            if(rate>maxUsedMemoryPercent){
                return true;
            }
        }
        return false;
    }
}
