package i2f.commons.component.spring.utils;

import i2f.commons.core.utils.trace.TraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ltb
 * @date 2021/8/10
 */
public class LogUtil {
    public static Logger getLogger(){
        String className= TraceUtil.getHereInvokerClass();
        Logger logger= LoggerFactory.getLogger(className);
        return logger;
    }
    public static Logger getMethodLogger(){
        String className=TraceUtil.getHereInvokerClass();
        String method=TraceUtil.getHereInvokerMethod();
        Logger logger= LoggerFactory.getLogger(className+"->"+method);
        return logger;
    }
    public static Logger getDebugLogger(){
        StackTraceElement elem=TraceUtil.getHereInvokerTrace();
        String tag= elem.getClassName()+"->"+elem.getMethodName()+" in file:"+elem.getFileName()+" of line:"+elem.getLineNumber();
        Logger logger=LoggerFactory.getLogger(tag);
        return logger;
    }
}
