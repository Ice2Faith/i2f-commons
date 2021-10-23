package i2f.commons.core.cache;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2021/10/18
 */
public interface ICacheCutPoint {
    Object getInvokeObject() throws Exception;
    Method getInvokeMethod() throws Exception;
    Object[] getInvokeArgs() throws Exception;
    Object invoke() throws Exception;
    Object invoke(Object[] args) throws Exception;
}
