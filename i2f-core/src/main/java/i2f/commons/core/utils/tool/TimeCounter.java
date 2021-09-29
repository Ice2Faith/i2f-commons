package i2f.commons.core.utils.tool;

public class TimeCounter {
    private volatile long start;
    private volatile long end;
    public TimeCounter(){
        start=System.currentTimeMillis();
    }
    public TimeCounter begin(){
        start=System.currentTimeMillis();
        return this;
    }
    public TimeCounter end(){
        end=System.currentTimeMillis();
        return this;
    }
    public long duration(){
        return this.end-this.start;
    }
}
