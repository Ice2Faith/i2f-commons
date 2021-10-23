package i2f.commons.core.cache.core;


import i2f.commons.core.cache.ICacheContainer;
import i2f.commons.core.cache.ICacheCutPoint;
import i2f.commons.core.cache.annotations.RemoveCache;
import i2f.commons.core.cache.annotations.SupportCache;
import i2f.commons.core.cache.core.base.CacheCore;
import i2f.commons.core.cache.data.CacheDataItem;
import i2f.commons.core.utils.reflect.core.resolver.base.AnnotationResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CacheWorker {
    @SupportCache(open = false,expire = 10*24*60*60*1000,ignoreObj = true,ignoreArgs = false)
    private static class CacheStaticMethodAnnotationHolder{

    }
    private static volatile SupportCache maxExpireSupportCache=null;
    public static SupportCache getMaxSupportCache(){
        if(maxExpireSupportCache!=null){
            return maxExpireSupportCache;
        }
        Class clazz=CacheStaticMethodAnnotationHolder.class;
        SupportCache maxExpire= AnnotationResolver.getClassAnnotation(clazz,false,false,SupportCache.class);
        if(maxExpireSupportCache==null){
            synchronized (CacheStaticMethodAnnotationHolder.class){
                maxExpireSupportCache=maxExpire;
            }
        }
        return maxExpireSupportCache;
    }
    public static int removeCaches(ICacheContainer container,SupportCache cache){
        RemoveCache[] rmCaches=cache.rmCaches();
        int count=CacheCore.removeCacheKeys(container,rmCaches);
        return count;
    }
    public static Object getCacheData(ICacheContainer container, Object ivkObj, Method method, Object ... args) throws Exception {
        SupportCache ann= CacheCore.getCacheAnnotation(method);
        if(ann==null || !ann.open()){
            return method.invoke(ivkObj,args);
        }

        removeCaches(container,ann);

        if(Modifier.isStatic(method.getModifiers())){
            ann=getMaxSupportCache();
        }

        CacheDataItem item= CacheCore.getCacheItem(container,ivkObj,method,args);
        if(CacheCore.hasHit(item,ann,ivkObj,method,args)){
            return item.cacheReturnVal;
        }

        Object obj=method.invoke(ivkObj,args);
        CacheDataItem newItem=CacheCore.setCacheItem(container,ann.expire(),obj,ivkObj,method,args);

        removeCaches(container,ann);
        return newItem.cacheReturnVal;
    }

    public static Object getCacheData(ICacheContainer container, ICacheCutPoint cutPoint) throws Exception {
        Object ivkObj=cutPoint.getInvokeObject();
        Method method= cutPoint.getInvokeMethod();
        Object[] args= cutPoint.getInvokeArgs();

        SupportCache ann=CacheCore.getCacheAnnotation(method);
        if(ann==null || !ann.open()){
            return cutPoint.invoke(args);
        }

        removeCaches(container,ann);

        CacheDataItem item=CacheCore.getCacheItem(container,ivkObj,method,args);
        if(CacheCore.hasHit(item,ann,ivkObj,method,args)){
            return item.cacheReturnVal;
        }

        Object obj=cutPoint.invoke(args);
        CacheDataItem newItem=CacheCore.setCacheItem(container,ann.expire(),obj,ivkObj,method,args);

        removeCaches(container,ann);
        return newItem.cacheReturnVal;
    }
}
