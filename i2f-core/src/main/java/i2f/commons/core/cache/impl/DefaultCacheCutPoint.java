package i2f.commons.core.cache.impl;

import i2f.commons.core.cache.ICacheCutPoint;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Data
@NoArgsConstructor
public class DefaultCacheCutPoint implements ICacheCutPoint {
    private Object ivkObj;
    private Method ivkMethod;
    private Object[] ivkArgs;

    public DefaultCacheCutPoint(Object ivkObj, Method ivkMethod, Object[] ivkArgs) {
        this.ivkObj = ivkObj;
        this.ivkMethod = ivkMethod;
        this.ivkArgs = ivkArgs;
    }

    @Override
    public Object getInvokeObject() throws Exception {
        return ivkObj;
    }

    @Override
    public Method getInvokeMethod() throws Exception {
        return ivkMethod;
    }

    @Override
    public Object[] getInvokeArgs() throws Exception {
        return ivkArgs;
    }

    @Override
    public Object invoke() throws Exception {
        return ivkMethod.invoke(ivkObj,ivkArgs);
    }

    @Override
    public Object invoke(Object[] args) throws Exception {
        return ivkMethod.invoke(ivkObj,args);
    }
}
