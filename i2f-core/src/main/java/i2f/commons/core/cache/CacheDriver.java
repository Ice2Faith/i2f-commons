package i2f.commons.core.cache;


import i2f.commons.core.cache.core.CacheWorker;

import java.lang.reflect.Method;

public class CacheDriver {

    public static Object getCacheData(ICacheContainer container, ICacheCutPoint cutPoint) throws Throwable {
        return CacheWorker.getCacheData(container, cutPoint);
    }

    public static Object getCacheData(ICacheContainer container, Object ivkObj, Method method,Object ... args) throws Exception{
        return CacheWorker.getCacheData(container,ivkObj,method,args);
    }

}
