package i2f.log.resolver;

import i2f.log.annotations.LogEntry;
import i2f.log.annotations.LogParam;
import i2f.log.enums.InvocationRecord;
import i2f.log.enums.InvocationType;
import i2f.log.enums.LogLevel;
import i2f.log.model.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author ltb
 * @date 2022/3/2 19:50
 * @desc
 */
public class LogResolver {
    public void resolve(BaseLogModel log, String type, LogLevel level, Object content){
        log.setDate(new Date());
        log.setType(type);
        log.setLevel(level);
        log.setContent(content);
    }
    public void resolve(BaseLocationLogModel log){
        StackTraceElement[] stack=Thread.currentThread().getStackTrace();
        resolve(log,stack,2);
    }
    public void resolve(BaseLocationLogModel log,int off){
        StackTraceElement[] stack=Thread.currentThread().getStackTrace();
        resolve(log,stack,2+off);
    }
    public void resolve(BaseLocationLogModel log,StackTraceElement[] stack,int off){
        if(off>stack.length){
            off= stack.length-1;
        }
        if(off<0) {
            off=0;
        }
        StackTraceElement elem=stack[off];
        log.setClassName(elem.getClassName());
        log.setMethod(elem.getMethodName());
        log.setFileName(elem.getFileName());
        log.setLine(elem.getLineNumber()+"");
        log.setThread(Thread.currentThread().getName());
    }
    public void resolve(ExceptionLogModel log,Throwable thr){
        log.setExceptionClassName(thr.getClass().getName());
        log.setExceptionMessage(thr.getMessage());
        Throwable cs=thr.getCause();
        if(cs!=null){
            log.setCauseClassName(cs.getClass().getName());
            log.setCauseMessage(cs.getMessage());
        }
    }
    public void resolve(SimpleBaseLogModel log,LogEntry mann){
        if(mann!=null){
            if(!"".equals(mann.system())) {
                log.setSystem(mann.system());
            }
            if(!"".equals(mann.module())) {
                log.setModule(mann.module());
            }
            if(!"".equals(mann.label())) {
                log.setLabel(mann.label());
            }
        }
    }

    public void resolve(SimpleBaseLogModel log,LogEntry mann,LogEntry cann){
        resolve(log,cann);
        resolve(log,mann);
    }

    public void resolve(InvocationLogModel log,Method method,Object[] args,Object retVal,Throwable ex){
        LogEntry mann=method.getAnnotation(LogEntry.class);
        LogEntry cann=method.getDeclaringClass().getAnnotation(LogEntry.class);
        resolve(log,mann,cann);

        log.setMethod(method.getName());
        log.setClassName(method.getDeclaringClass().getName());

        Set<InvocationType> types=new HashSet<>();
        if(mann!=null){
            for(InvocationType item : mann.entries()){
                types.add(item);
            }
        }else{
            if(cann!=null){
                for(InvocationType item : cann.entries()){
                    types.add(item);
                }
            }
        }
        String invokeType="";
        for(InvocationType item : types){
            invokeType+=","+item;
        }
        if(invokeType.length()>0){
            invokeType = invokeType.substring(1);
            log.setInvokeType(invokeType);
        }

        Set<InvocationRecord> records=new HashSet<>();
        if(mann!=null){
            for(InvocationRecord item : mann.records()){
                records.add(item);
            }
        }else{
            if(cann!=null){
                for(InvocationRecord item : cann.records()){
                    records.add(item);
                }
            }
        }

        String invokeRecord="";
        for(InvocationRecord item : records){
            invokeRecord+=","+item;
        }
        if(invokeRecord.length()>0){
            invokeRecord = invokeRecord.substring(1);
            log.setInvokeRecord(invokeRecord);
        }

        if(retVal==null && ex==null){
            log.setBeginDate(new Date());
        }else{
            log.setEndDate(new Date());
        }

        if(records.contains(InvocationRecord.ARGUMENT)){
            if(args!=null){
                List<Map.Entry<String,Object>> ivkArgs=new ArrayList<>();
                Parameter[] params=method.getParameters();
                for(int i=0;i< params.length;i++){
                    Parameter item=params[i];
                    LogParam pann=item.getAnnotation(LogParam.class);
                    String paramName=item.getName();
                    if(pann!=null){
                        if(!"".equals(pann.value())){
                            paramName=pann.value();
                        }
                    }
                    Object arg=args[i];
                    if(i==params.length-1 && params.length< args.length){
                        Object[] argList=new Object[args.length-params.length];
                        for(int j=0;j<argList.length;j++){
                            argList[j]=args[params.length+j];
                        }
                        arg=argList;
                    }
                    ivkArgs.add(new AbstractMap.SimpleEntry<>(paramName,arg));
                }
                log.setInvokeArgs(ivkArgs);
            }
        }

        if(records.contains(InvocationRecord.RETURN)){
            if(retVal!=null){
                log.setReturnValue(retVal);
            }
        }

        if(records.contains(InvocationRecord.EXCEPTION)){
            if(ex!=null){
                resolve(log,ex);
            }
        }
    }

}
