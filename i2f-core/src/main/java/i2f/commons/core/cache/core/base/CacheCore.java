package i2f.commons.core.cache.core.base;


import i2f.commons.core.cache.ICacheContainer;
import i2f.commons.core.cache.annotations.RemoveCache;
import i2f.commons.core.cache.annotations.SupportCache;
import i2f.commons.core.cache.data.CacheDataItem;
import i2f.commons.core.utils.reflect.ReflectUtil;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CacheCore {
    public static long cleanExpire(ICacheContainer container){
        long ret=0;
        Set<String> keys= container.keys();
        long nowTimeOut=new Date().getTime();
        for(String key : keys){
            CacheDataItem item=container.get(key);
            if(item.cacheExpireTime<=nowTimeOut){
                container.del(key);
                ret++;
            }
        }
        return ret;
    }

    public static int removeCacheKeys(ICacheContainer container, RemoveCache ... rmCaches){
        if(container==null || rmCaches==null || rmCaches.length==0){
            return -1;
        }
        int count=0;
        for(RemoveCache item : rmCaches){
            String className=item.forCls().getName();
            String methodNameRegex=item.forMethodRegex();
            String methodGenericString= item.forMethodGenericStringRegex();
            className=className.replaceAll("\\.","\\\\.");
            String patten="class\\("+className+"\\)method\\(.*\\."+methodNameRegex+"\\(.*\\).*\\).*";
            if(methodGenericString!=null && !"".equals(methodGenericString)){
                patten="class\\("+className+"\\)method\\("+methodNameRegex+"\\).*";
            }
            List<String> keys=container.keys(patten);
            for(String key : keys){
                container.del(key);
                count++;
            }
        }
        return count;
    }

    public static String genCacheKey(CacheDataItem item) {
        AppendUtil.AppendBuilder buffer= AppendUtil.buffer()
                .add("class(")
                .add(item.ivkMethod.getDeclaringClass().getName())
                .add(")")
                .add("method(")
                .add(item.ivkMethod.toGenericString())
                .add(")");
        if(CheckUtil.notEmptyArray(item.ivkArgs)){
            for(int i=0;i<item.ivkArgs.length;i++){
                Object obj=item.ivkArgs[i];
                if(obj==null || obj instanceof Number || obj instanceof Boolean){
                    buffer.adds(i,":",String.valueOf(obj));
                }
                else if(obj instanceof String){
                    String str=(String)obj;
                    int len=str.length();
                    if(len>20){
                        buffer.adds(i,":",str.substring(0,10),"...",str.substring(len-10+3,len));
                    }
                    else{
                        buffer.adds(i,":",str);
                    }
                }
                else if(obj instanceof Date){
                    buffer.adds(i,":",((Date)obj).getTime());
                }else if(obj instanceof Class){
                    buffer.adds(i,":",((Class)obj).getSimpleName());
                }else if(obj instanceof Method){
                    buffer.adds(i,":",((Method)obj).getName());
                }else if(obj instanceof Field){
                    buffer.adds(i,":",((Field)obj).getName());
                }
            }
        }

        return buffer.done();
    }

    public static CacheDataItem getCacheItem(ICacheContainer container, Object ivkObj, Method method, Object[] args) {
        CacheDataItem item = new CacheDataItem();
        item.ivkMethod = method;
        item.ivkArgs = args;
        item.ivkObj = ivkObj;
        String key = genCacheKey(item);
        return container.get(key);
    }

    public static CacheDataItem setCacheItem(ICacheContainer container,long expireTime, Object returnVal, Object ivkObj, Method method, Object[] args) {
        CacheDataItem item = new CacheDataItem();
        item.ivkMethod = method;
        item.ivkArgs = args;
        item.ivkObj = ivkObj;
        item.cacheReturnVal = returnVal;
        item.setExpireMillSecond(expireTime);
        String key = genCacheKey(item);
        container.set(key, item);
        return item;
    }

    public static SupportCache getCacheAnnotation(Method method) {
        return ReflectUtil.getMethodAnnotationCheckClass(method, SupportCache.class);
    }

    public static boolean hasHit(CacheDataItem item, SupportCache ann, Object ivkObj, Method method, Object[] args) {
        if (item == null) {
            return false;
        }
        if (item.cacheExpireTime <= new Date().getTime()) {
            return false;
        }
        if (!ann.ignoreObj()) {
            if (ivkObj != null) {
                if (!ivkObj.equals(item.ivkObj)) {
                    return false;
                }
            }
        }

        if (!method.equals(item.ivkMethod)) {
            return false;
        }

        if (!ann.ignoreArgs()) {
            if (args != null && item.ivkArgs != null) {
                if (item.ivkArgs.length != args.length) {
                    return false;
                }
                for (int i = 0; i < args.length; i++) {
                    if (args[i] != null) {
                        if (item.ivkArgs[i] == null) {
                            continue;
                        }
                        if (!CheckUtil.isEquals(true, args[i], item.ivkArgs[i])) {
                            return false;
                        }
                    }
                }
            } else if (args != null && item.ivkArgs == null) {
                return false;
            } else if (args == null && item.ivkArgs != null) {
                return false;
            }

        }

        return true;
    }

}
