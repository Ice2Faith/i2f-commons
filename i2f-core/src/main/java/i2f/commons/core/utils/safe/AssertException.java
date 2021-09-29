package i2f.commons.core.utils.safe;


import i2f.commons.core.exception.CommonsException;
import i2f.commons.core.utils.trace.TraceUtil;

public class AssertException extends CommonsException {

    public AssertException() {

    }

    {
        clazzName= TraceUtil.getHereInvokerClass();
        methodName=TraceUtil.getHereInvokerMethod();
        fileName=TraceUtil.getHereInvokerFileName();
        lineNumber=TraceUtil.getHereInvokerLineNumber();
    }

    public AssertException(String message) {
        super(message);
    }

    public AssertException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertException(Throwable cause) {
        super(cause);
    }

    public AssertException(int code, String message) {
        super(code, message);
    }

    public AssertException(int code, String message, Object obj) {
        super(code, message, obj);
    }

    public AssertException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AssertException(int code, String message, Throwable cause, Object obj) {
        super(code, message, cause, obj);
    }

    public AssertException(int code, String message, Throwable cause, Object obj, String clazzName) {
        super(code, message, cause, obj, clazzName);
    }
}
