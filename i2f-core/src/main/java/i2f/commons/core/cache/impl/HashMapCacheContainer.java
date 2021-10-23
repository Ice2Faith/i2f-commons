package i2f.commons.core.cache.impl;


import i2f.commons.core.cache.ICacheContainer;
import i2f.commons.core.cache.data.CacheDataItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapCacheContainer implements ICacheContainer {
    public Map<String, CacheDataItem> map=new ConcurrentHashMap<String,CacheDataItem>();
    @Override
    public CacheDataItem get(String key) {
        return map.get(key);
    }

    @Override
    public void set(String key, CacheDataItem val) {
        map.put(key,val);
    }

    @Override
    public boolean hasCached(String key) {
        return map.containsKey(key);
    }

    @Override
    public CacheDataItem del(String key) {
        return map.remove(key);
    }

    @Override
    public List<String> keys(String regex) {
        List<String> ret=new LinkedList<>();
        Set<String> allKey=map.keySet();
        for(String item : allKey){
            if(item.matches(regex)){
                ret.add(item);
            }
        }
        return ret;
    }

    @Override
    public long size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keys() {
        return map.keySet();
    }
}
