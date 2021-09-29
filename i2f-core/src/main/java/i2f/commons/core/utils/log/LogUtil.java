package i2f.commons.core.utils.log;


import i2f.commons.core.utils.str.AppendUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class LogUtil {
    public interface LogLevel{
        String INFO="info";
        String WARNING="warning";
        String DEBUG="debug";
        String ERROR="error";
    }
    private static ConcurrentHashMap<String,Boolean> logLevelControl=new ConcurrentHashMap<>();
    private static ByteArrayOutputStream bos=new ByteArrayOutputStream();
    private static PrintStream print=new PrintStream(bos);
    private static SimpleDateFormat sdfmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    public static synchronized void closeAllLevel(){
        logLevelControl.put(LogLevel.INFO, false);
        logLevelControl.put(LogLevel.WARNING, false);
        logLevelControl.put(LogLevel.DEBUG, false);
        logLevelControl.put(LogLevel.ERROR, false);
    }
    public static synchronized void openAllLevel(){
        logLevelControl.put(LogLevel.INFO, true);
        logLevelControl.put(LogLevel.WARNING, true);
        logLevelControl.put(LogLevel.DEBUG, true);
        logLevelControl.put(LogLevel.ERROR, true);
    }
    public static synchronized void setLogLevelControl(String level,Boolean state){
        if(state==null){
            return;
        }
        logLevelControl.put(level, state);
    }
    private static synchronized void setDateFormat(String format){
        sdfmt=new SimpleDateFormat(format);
    }

    public static void info(Object invoker,Object ... args){
        info(invoker.getClass(),args);
    }
    public static void warning(Object invoker,Object ... args){
        warning(invoker.getClass(), args);
    }
    public static void debug(Object invoker,Object ... args){
        debug(invoker.getClass(), args);
    }
    public static void error(Object invoker,Object ... args){
        error(invoker.getClass(), args);
    }

    public static void info(Class clazz,Object ... args){
        logProxy(LogLevel.INFO,clazz,args);
    }
    public static void warning(Class clazz,Object ... args){
        logProxy(LogLevel.WARNING,clazz,args);
    }
    public static void debug(Class clazz,Object ... args){
        logProxy(LogLevel.DEBUG,clazz,args);
    }
    public static void error(Class clazz,Object ... args){
        logProxy(LogLevel.ERROR,clazz,args);
    }

    private synchronized static void logProxy(String level,Class clazz,Object ... args){
        Boolean openLevel=logLevelControl.get(level);
        if(true==openLevel){
            AppendUtil.AppendBuilder buffer= AppendUtil.buffer();
            buffer.adds("[",level," ][",sdfmt.format(new Date()),"] : ",clazz.getName()," : ");
            String argsStr="";
            for(int i=0;i< args.length;i+=1){
                if(i!=0){
                    buffer.add(" , ");
                }
                buffer.add(args[i]);
            }
            print.println(buffer.done());
            print.flush();
            synchronized (LogUtil.class){
                try{
                    if(bos.size()>0){
                        System.out.print(bos.toString());
                        if(fos!=null){
                            fos.write(bos.toByteArray());
                            fos.flush();
                        }
                        bos.reset();
                    }
                    Thread.sleep(500);
                }catch(Exception e){

                }
            }
        }
    }
    private static FileOutputStream fos;
    static {
        logLevelControl.put(LogLevel.INFO,true);
        logLevelControl.put(LogLevel.WARNING,true);
        logLevelControl.put(LogLevel.DEBUG,true);
        logLevelControl.put(LogLevel.ERROR,true);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    synchronized (LogUtil.class){
                        if(print!=null){
                            print.flush();
                            print.close();
                        }
                        if(fos!=null){
                            fos.flush();
                            fos.close();
                        }
                    }
                }catch(Exception e){

                }
            }
        }));
    }

    public synchronized static void setLogFile(String fileName){
        if(fos!=null){
            try{
                fos.flush();
                fos.close();
            }catch(Exception e){
                fos=null;
            }
        }
        try {
            fos=new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            fos=null;
        }
    }
}
