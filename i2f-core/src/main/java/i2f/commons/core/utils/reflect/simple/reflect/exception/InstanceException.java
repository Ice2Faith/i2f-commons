package i2f.commons.core.utils.reflect.simple.reflect.exception;

import i2f.commons.core.utils.reflect.simple.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/17 10:54
 * @desc
 */
public class InstanceException extends ReflectException {
    public InstanceException() {
    }

    public InstanceException(String message) {
        super(message);
    }

    public InstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceException(Throwable cause) {
        super(cause);
    }
}
