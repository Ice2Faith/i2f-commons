package i2f.commons.core.validator.exception;


import i2f.commons.core.exception.CommonsException;
import i2f.commons.core.utils.trace.TraceUtil;

public class ValidateException extends CommonsException {
    public ValidateException() {
    }

    {
        clazzName= TraceUtil.getHereInvokerClass();
        methodName=TraceUtil.getHereInvokerMethod();
        fileName=TraceUtil.getHereInvokerFileName();
        lineNumber=TraceUtil.getHereInvokerLineNumber();
    }
    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }

    public ValidateException(int code, String message) {
        super(code, message);
    }

    public ValidateException(int code, String message, Object obj) {
        super(code, message, obj);
    }

    public ValidateException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ValidateException(int code, String message, Throwable cause, Object obj) {
        super(code, message, cause, obj);
    }

    public ValidateException(int code, String message, Throwable cause, Object obj, String clazzName) {
        super(code, message, cause, obj, clazzName);
    }

    public static void thrCatch(String message,Throwable cause,Object obj) throws ValidateException {
        throw new ValidateException(ErrCode.CATCH,message,cause,obj);
    }

    public static void thrThrow(String message,Object obj) throws ValidateException {
        throw new ValidateException(ErrCode.THROW,message,obj);
    }

    public static ValidateException cat(String message,Throwable cause,Object obj)  {
        return new ValidateException(ErrCode.CATCH,message,cause,obj);
    }

    public static ValidateException thr(String message,Object obj)  {
        return new ValidateException(ErrCode.THROW,message,obj);
    }
}
