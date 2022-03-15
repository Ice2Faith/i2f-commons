package i2f.commons.core.utils.reflect.simple.reflect.exception;


import i2f.commons.core.utils.reflect.simple.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
public class ReadOnlyAccessException extends ReflectException {
    public ReadOnlyAccessException() {
    }

    public ReadOnlyAccessException(String message) {
        super(message);
    }

    public ReadOnlyAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadOnlyAccessException(Throwable cause) {
        super(cause);
    }
}
