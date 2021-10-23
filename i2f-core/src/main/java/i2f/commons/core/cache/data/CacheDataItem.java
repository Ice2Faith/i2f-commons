package i2f.commons.core.cache.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Date;

@Data
@NoArgsConstructor
public class CacheDataItem{
    public long cacheExpireTime = new Date().getTime();

    public Method ivkMethod;
    public Object ivkObj;

    public Object[] ivkArgs;
    public Object cacheReturnVal;

    public CacheDataItem(Method ivkMethod,Object cacheReturnVal){
        this.ivkMethod=ivkMethod;
        this.cacheReturnVal=cacheReturnVal;
    }

    public CacheDataItem(Method ivkMethod,Object cacheReturnVal,Object[] ivkArgs){
        this.ivkMethod=ivkMethod;
        this.cacheReturnVal=cacheReturnVal;
        this.ivkArgs=ivkArgs;
    }

    public CacheDataItem(Method ivkMethod,Object cacheReturnVal,Object ivkObj,Object[] ivkArgs){
        this.ivkMethod=ivkMethod;
        this.cacheReturnVal=cacheReturnVal;
        this.ivkObj=ivkObj;
        this.ivkArgs=ivkArgs;
    }

    public CacheDataItem setExpireMillSecond(long expireMillSecond){
        this.cacheExpireTime=new Date().getTime()+expireMillSecond;
        return this;
    }
}
