package i2f.commons.core.utils.reflect.simple.reflect.exception;


import i2f.commons.core.utils.reflect.simple.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
public class FieldAccessException extends ReflectException {
    public FieldAccessException() {
    }

    public FieldAccessException(String message) {
        super(message);
    }

    public FieldAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldAccessException(Throwable cause) {
        super(cause);
    }
}
