package i2f.commons.core.utils.str;

/**
 * @author ltb
 * @date 2021/9/28
 */
public interface IAppendMap<T> {
    void append(T val, Appendable appender,Object ... params);
}
