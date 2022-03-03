package i2f.log.impl;

import i2f.log.AbsLoggerAdapter;
import i2f.log.api.LogWriter;
import i2f.log.model.BaseLogModel;
import i2f.log.model.ExceptionLogModel;
import i2f.log.resolver.LogResolver;

/**
 * @author ltb
 * @date 2022/3/3 10:06
 * @desc
 */
public class SimpleLogger extends AbsLoggerAdapter implements ObjectContentLogger {
    private LogWriter writer;
    private String className;
    private String system;
    private String module;
    private String label;
    public SimpleLogger(LogWriter writer){
        this.writer=writer;
    }
    public SimpleLogger(LogWriter writer,String className,String system,String module){
        this.writer=writer;
        this.system=system;
        this.module=module;
        this.className=className;
    }
    @Override
    public void write(BaseLogModel log) {
        writer.write(log);
    }

    public SimpleLogger label(String label){
        this.label=label;
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
}
