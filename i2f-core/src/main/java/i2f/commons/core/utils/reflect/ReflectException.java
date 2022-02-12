package i2f.commons.core.utils.reflect;

/**
 * @author ltb
 * @date 2022/1/26 15:11
 * @desc
 */
public class ReflectException extends RuntimeException {
    public ReflectException() {
        super();
    }

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}
