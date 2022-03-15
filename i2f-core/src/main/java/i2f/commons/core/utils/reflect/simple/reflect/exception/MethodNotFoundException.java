package i2f.commons.core.utils.reflect.simple.reflect.exception;


import i2f.commons.core.utils.reflect.simple.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
public class MethodNotFoundException extends ReflectException {
    public MethodNotFoundException() {
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }
}
