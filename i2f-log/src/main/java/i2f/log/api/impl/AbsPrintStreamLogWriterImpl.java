package i2f.log.api.impl;

import i2f.log.api.BaseLogWriter;
import i2f.log.model.*;

import java.text.SimpleDateFormat;

/**
 * @author ltb
 * @date 2022/3/2 15:21
 * @desc
 */
public abstract class AbsPrintStreamLogWriterImpl extends BaseLogWriter {
    protected abstract void writeLine(String str);
    public static ThreadLocal<SimpleDateFormat> fmt=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    });
    @Override
    public void write(BaseLogModel log) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s [%-5s] -%-5s @%-35s ",
                fmt.get().format(log.getTime()),
                log.getLevel(),
                log.getType(),
                log.getLocation()
        ));
        if(log instanceof SimpleBaseLogModel){
            SimpleBaseLogModel model=(SimpleBaseLogModel)log;
            builder.append(String.format(" on system:%s module:%s label:%s",
                    model.getSystem(),
                    model.getModule(),
                    model.getLabel()));
        }
        if(log instanceof BaseLocationLogModel){
            BaseLocationLogModel model=(BaseLocationLogModel)log;
            builder.append(String.format(" at %s.%s(%s):@%-4s->%s",
                    model.getClassName(),
                    model.getMethod(),
                    model.getThread(),
                    model.getFileName(),
                    model.getLine()));
        }
        builder.append(String.format(" : %s",
                log.getContent()));

        if(log instanceof ExceptionLogModel){
            ExceptionLogModel model=(ExceptionLogModel)log;
            builder.append(String.format(" ex is: %s msg: %s",
                    model.getExceptionClassName(),
                    model.getExceptionMessage()));
        }

        if(log instanceof InvocationLogModel){
            InvocationLogModel model=(InvocationLogModel)log;
            builder.append(String.format(" ivk type: %s begin: %s end: %s return:%s args: %s",
                    model.getInvokeType(),
                    model.getBeginDate(),
                    model.getEndDate(),
                    model.getReturnValue()+"",
                    model.getInvokeArgs()+""));
        }

        if(log instanceof RemoteInvokeLogModel){
            RemoteInvokeLogModel model=(RemoteInvokeLogModel)log;
            builder.append(String.format(" remote url: %s begin: %s end: %s method: %s request: %s response:%s",
                    model.getRequestUrl(),
                    model.getRequestDate(),
                    model.getResponseDate(),
                    model.getRequestMethod(),
                    model.getRequestContent()+"",
                    model.getResponseContent()+""));
        }

        String str=builder.toString();
        writeLine(str);
    }
}
