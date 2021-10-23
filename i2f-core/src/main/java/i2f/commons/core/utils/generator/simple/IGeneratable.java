package i2f.commons.core.utils.generator.simple;

/**
 * @author ltb
 * @date 2021/9/11
 */
public interface IGeneratable {
    Object getData();
    String toGen();
    static String gen(Object obj){
        if(obj instanceof IGeneratable){
            return ((IGeneratable) obj).toGen();
        }
        return String.valueOf(obj);
    }
}
