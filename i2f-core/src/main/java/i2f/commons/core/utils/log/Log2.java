package i2f.commons.core.utils.log;

import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.trace.TraceUtil;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log2 {
    public static volatile PrintStream ops;
    public static volatile PrintStream ps=System.out;
    public static volatile SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    public static void i(Object ... objs){
        StackTraceElement stack= TraceUtil.getHereInvokerTrace();
        o("INFO   ",stack,objs);
    }
    public static void w(Object ... objs){
        StackTraceElement stack=TraceUtil.getHereInvokerTrace();
        o("WARNING",stack,objs);
    }
    public static void e(Object ... objs){
        StackTraceElement stack=TraceUtil.getHereInvokerTrace();
        o("ERROR  ",stack,objs);
    }

    public static void i(StackTraceElement tarStack,Object ... objs){
        o("INFO   ",tarStack,objs);
    }
    public static void w(StackTraceElement tarStack,Object ... objs){
        o("WARNING",tarStack,objs);
    }
    public static void e(StackTraceElement tarStack,Object ... objs){
        o("ERROR  ",tarStack,objs);
    }

    synchronized private static void o(String level,StackTraceElement stack,Object ... objs){
        AppendUtil.AppendBuilder buffer= AppendUtil.buffer();
        if(stack!=null) {
            buffer.adds("[", level, "]", " ", "[", fmt.format(new Date()), "]", " ", stack.getClassName(), ">", stack.getMethodName(), " line:", stack.getLineNumber(), " : ");
        }else{
            buffer.adds( "[", level, "]", " ", "[", fmt.format(new Date()), "]",  " : ");
        }
        buffer.adds(objs);
        String line= buffer.done();
        ps.println(line);
        ps.flush();
        if(ops!=null){
            ops.println(line);
            ops.flush();
        }
    }
}
