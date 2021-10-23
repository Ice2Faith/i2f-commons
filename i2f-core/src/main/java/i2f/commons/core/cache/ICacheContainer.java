package i2f.commons.core.cache;


import i2f.commons.core.cache.data.CacheDataItem;

import java.util.List;
import java.util.Set;

public interface ICacheContainer {
    CacheDataItem get(String key);
    void set(String key,CacheDataItem val);
    boolean hasCached(String key);
    CacheDataItem del(String key);
    List<String> keys(String regex);
    long size();
    void clear();
    Set<String> keys();
}
