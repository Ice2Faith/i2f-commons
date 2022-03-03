package i2f.log.enums;

/**
 * @author ltb
 * @date 2022/3/3 9:52
 * @desc
 */
public enum LogLevel {
    OFF(0),FATAL(1),ERROR(2),WARN(3),INFO(4),DEBUG(5),ALL(10);
    private int level;
    private LogLevel(int level){
        this.level=level;
    }
    public int level(){
        return this.level;
    }
}
