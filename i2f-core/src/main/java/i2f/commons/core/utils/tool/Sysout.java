package i2f.commons.core.utils.tool;

import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.trace.TraceUtil;

import java.io.PrintStream;

/**
 * @author ltb
 * @date 2021/8/12
 */
public class Sysout {
    public static PrintStream out=System.out;
    public static String DEFAULT_SEPARATOR=",";
    public static void line(Object ... args){
        out.println(AppendUtil.builder().adds(args).done());
    }
    public static void lineSep(Object ... args){
        out.println(AppendUtil.builder().addsSep(DEFAULT_SEPARATOR,args).done());
    }
    public static void lineSep(String sep,Object ... args){
        out.println(AppendUtil.builder().addsSep(sep,args).done());
    }
    public static void val(String valName, Object val){
        String type="";
        if(val!=null){
            Class clazz=val.getClass();
            type= clazz.getSimpleName();
        }
        out.println(AppendUtil.builder().adds(valName,"(",type,")","=",val).done());
    }
    public static void log(Object ... args){
        StackTraceElement elem= TraceUtil.getHereInvokerTrace();
        out.println(AppendUtil.builder().adds(elem.getClassName(),"->",elem.getMethodName()," line:"+elem.getLineNumber()," : ").adds(args).done());
    }
    public static void logSep(Object ... args){
        StackTraceElement elem= TraceUtil.getHereInvokerTrace();
        out.println(AppendUtil.builder().adds(elem.getClassName(),"->",elem.getMethodName()," line:"+elem.getLineNumber()," : ").addsSep(DEFAULT_SEPARATOR,args).done());
    }
    public static void logSep(String sep,Object ... args){
        StackTraceElement elem= TraceUtil.getHereInvokerTrace();
        out.println(AppendUtil.builder().adds(elem.getClassName(),"->",elem.getMethodName()," line:"+elem.getLineNumber()," : ").addsSep(sep,args).done());
    }
}
