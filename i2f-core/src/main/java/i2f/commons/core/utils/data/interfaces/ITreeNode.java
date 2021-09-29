package i2f.commons.core.utils.data.interfaces;

/**
 * @author ltb
 * @date 2021/9/29
 */
public interface ITreeNode<T> {
    void asMyChild(T val);
    boolean isMyChild(T val);
    boolean isMyParent(T val);
}
