package i2f.log.impl;

import i2f.log.AbsLoggerAdapter;
import i2f.log.LogContext;
import i2f.log.model.BaseLogModel;
import i2f.log.model.ExceptionLogModel;
import i2f.log.resolver.LogResolver;

/**
 * @author ltb
 * @date 2022/3/3 10:06
 * @desc
 */
public class SimpleLogger extends AbsLoggerAdapter implements ObjectContentLogger {
    private String className;
    private String system;
    private String module;
    private String label;
    private String type;

    public SimpleLogger(String className,String system,String module){
        this.system=system;
        this.module=module;
        this.className=className;
    }
    @Override
    public void write(BaseLogModel log) {
        if(LogContext.decision.decision(log)) {
            LogContext.writer.write(log);
        }
    }

    public SimpleLogger label(String label){
        this.label=label;
        return this;
    }

    public SimpleLogger type(String type){
        this.type=type;
        return this;
    }

    protected ExceptionLogModel proxy(Object content){
        ExceptionLogModel log=new ExceptionLogModel();
        LogResolver resolver=new LogResolver();
        resolver.resolve(log,null,null,content);
        resolver.resolve(log,2);
        log.setClassName(className);
        log.setSystem(system);
        log.setModule(module);
        log.setLabel(label);
        log.setType(type);
        return log;
    }

    @Override
    public void fatal(Object content) {
        fatal(proxy(content));
    }

    @Override
    public void error(Object content) {
        error(proxy(content));
    }

    @Override
    public void warn(Object content) {
        warn(proxy(content));
    }

    @Override
    public void info(Object content) {
        info(proxy(content));
    }

    @Override
    public void debug(Object content) {
        debug(proxy(content));
    }

    @Override
    public void trace(Object content) {
        trace(proxy(content));
    }
}
