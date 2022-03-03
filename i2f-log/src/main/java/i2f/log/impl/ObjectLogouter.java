package i2f.log.impl;

import i2f.log.Logger;

/**
 * @author ltb
 * @date 2022/3/3 10:09
 * @desc
 */
public interface ObjectLogouter extends Logger {
    void fatal(Object content);
    void error(Object content);
    void warn(Object content);
    void info(Object content);
    void debug(Object content);
}
