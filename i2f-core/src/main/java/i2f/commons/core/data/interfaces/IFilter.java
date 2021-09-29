package i2f.commons.core.data.interfaces;

/**
 * @author ltb
 * @date 2021/9/28
 */
public interface IFilter<T> {
    boolean choice(T val);
}
