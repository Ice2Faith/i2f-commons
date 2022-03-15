package i2f.commons.core.utils.reflect.simple.reflect.exception;


import i2f.commons.core.utils.reflect.simple.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
public class NullAccessException extends ReflectException {
    public NullAccessException() {
    }

    public NullAccessException(String message) {
        super(message);
    }

    public NullAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullAccessException(Throwable cause) {
        super(cause);
    }
}
