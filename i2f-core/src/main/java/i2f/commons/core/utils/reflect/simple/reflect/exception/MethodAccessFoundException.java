package i2f.commons.core.utils.reflect.simple.reflect.exception;


import i2f.commons.core.utils.reflect.simple.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
public class MethodAccessFoundException extends ReflectException {
    public MethodAccessFoundException() {
    }

    public MethodAccessFoundException(String message) {
        super(message);
    }

    public MethodAccessFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodAccessFoundException(Throwable cause) {
        super(cause);
    }
}
