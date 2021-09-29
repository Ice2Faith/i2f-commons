package i2f.commons.core.data.interfaces;

/**
 * @author ltb
 * @date 2021/9/28
 */
public interface ICombine<F,S,T> {
    T combine(F fval,S sval);
}
