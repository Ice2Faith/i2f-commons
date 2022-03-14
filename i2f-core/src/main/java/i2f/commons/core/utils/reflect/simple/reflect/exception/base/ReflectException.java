package i2f.commons.core.utils.reflect.simple.reflect.exception.base;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
public class ReflectException extends RuntimeException {
    public ReflectException() {
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
