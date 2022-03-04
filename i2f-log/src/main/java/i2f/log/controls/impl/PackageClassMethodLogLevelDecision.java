package i2f.log.controls.impl;

import i2f.log.Environment;
import i2f.log.controls.LogLevelDecision;
import i2f.log.enums.LogLevel;
import i2f.log.model.BaseLocationLogModel;
import i2f.log.model.BaseLogModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/3 14:02
 * @desc
 */
public class PackageClassMethodLogLevelDecision implements LogLevelDecision {
    private ConcurrentHashMap<String, LogLevel> decisionMap=new ConcurrentHashMap<>();

    @Override
    public boolean decision(BaseLogModel log) {
        LogLevel logLevel=log.getLevel();
        String location=log.getClassName();
        if(log instanceof BaseLocationLogModel){
            BaseLocationLogModel model=(BaseLocationLogModel)log;
            location=model.getClassName()+"."+model.getMethod();
        }
        for(Map.Entry<String,LogLevel> item : decisionMap.entrySet()){
            String patten=item.getKey();
            LogLevel level=item.getValue();
            if(location.matches(patten)){
                return logLevel.level()<=level.level();
            }
        }
        return logLevel.level() <= Environment.projectDefaultLogLevel.level();
    }

    public void registerRule(String locationPatten,LogLevel level){
        decisionMap.put(locationPatten, level);
    }
}
