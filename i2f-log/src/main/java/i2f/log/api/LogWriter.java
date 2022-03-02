package i2f.log.api;

import i2f.log.model.BaseLogModel;

/**
 * @author ltb
 * @date 2022/3/2 15:05
 * @desc
 */
public interface LogWriter {
    void write(BaseLogModel log);
}
