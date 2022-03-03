package i2f.log;

import i2f.log.enums.LogLevel;
import i2f.log.model.BaseLogModel;

/**
 * @author ltb
 * @date 2022/3/3 10:02
 * @desc
 */
public abstract class AbsLoggerAdapter implements Logger {
    public abstract void write(BaseLogModel log);
    protected void proxy(LogLevel level,BaseLogModel log){
        log.setLevel(level);
        write(log);
    }
    @Override
    public void fatal(BaseLogModel log) {
        proxy(LogLevel.FATAL,log);
    }

    @Override
    public void error(BaseLogModel log) {
        proxy(LogLevel.ERROR,log);
    }

    @Override
    public void warn(BaseLogModel log) {
        proxy(LogLevel.WARN,log);
    }

    @Override
    public void info(BaseLogModel log) {
        proxy(LogLevel.INFO,log);
    }

    @Override
    public void debug(BaseLogModel log) {
        proxy(LogLevel.DEBUG,log);
    }
}
