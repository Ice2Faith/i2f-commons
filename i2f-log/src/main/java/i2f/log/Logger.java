package i2f.log;

import i2f.log.model.BaseLogModel;

/**
 * @author ltb
 * @date 2022/3/3 9:59
 * @desc
 */
public interface Logger {
    void fatal(BaseLogModel log);
    void error(BaseLogModel log);
    void warn(BaseLogModel log);
    void info(BaseLogModel log);
    void debug(BaseLogModel log);
}
