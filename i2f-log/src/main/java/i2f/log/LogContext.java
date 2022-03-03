package i2f.log;

import i2f.log.api.impl.MulticastLogWriter;
import i2f.log.controls.impl.PackageClassMethodLogLevelDecision;

/**
 * @author ltb
 * @date 2022/3/3 13:55
 * @desc
 */
public class LogContext {
    public static MulticastLogWriter writer=new MulticastLogWriter();

    public static PackageClassMethodLogLevelDecision decision=new PackageClassMethodLogLevelDecision();


}
