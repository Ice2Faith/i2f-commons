package i2f.log.enums;

/**
 * @author ltb
 * @date 2022/3/3 9:52
 * @desc
 */
public enum LogLevel {
    OFF(0),FATAL(1),ERROR(2),WARN(3),INFO(4),DEBUG(5),TRACE(6),ALL(10);
    private int level;
    private LogLevel(int level){
        this.level=level;
    }
    public int level(){
        return this.level;
    }
    public static LogLevel parse(String lvl){
        if(lvl==null){
            return null;
        }
        lvl=lvl.toLowerCase();
        if("off".equals(lvl)){
            return OFF;
        }
        if("fatal".equals(lvl)){
            return FATAL;
        }
        if("error".equals(lvl)){
            return ERROR;
        }
        if("warn".equals(lvl)){
            return WARN;
        }
        if("info".equals(lvl)){
            return INFO;
        }
        if("trace".equals(lvl)){
            return TRACE;
        }
        if("all".equals(lvl)){
            return ALL;
        }
        return null;
    }
}
