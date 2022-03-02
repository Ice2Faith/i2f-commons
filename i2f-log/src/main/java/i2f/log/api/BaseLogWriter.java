package i2f.log.api;

import i2f.log.annotations.LogEntry;
import i2f.log.model.*;

/**
 * @author ltb
 * @date 2022/3/2 15:22
 * @desc
 */
public abstract class BaseLogWriter implements LogWriter{
    public void write(BaseLogModel log, LogEntry entry){
        if(log instanceof SimpleBaseLogModel){
            SimpleBaseLogModel model=(SimpleBaseLogModel)log;
            if(!"".equals(entry.system())){
                model.setSystem(entry.system());
            }
            if(!"".equals(entry.module())){
                model.setModule(entry.module());
            }
            if(!"".equals(entry.label())){
                model.setLabel(entry.label());
            }
        }
    }
}
