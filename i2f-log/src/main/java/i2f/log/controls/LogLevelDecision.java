package i2f.log.controls;

import i2f.log.model.BaseLogModel;

/**
 * @author ltb
 * @date 2022/3/3 13:59
 * @desc
 */
public interface LogLevelDecision {
    boolean decision(BaseLogModel log);
}
