package i2f.commons.core.exception;

import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.trace.TraceUtil;

public class CommonsRuntimeException extends RuntimeException{
    public interface ErrCode{
        int EMPTY=0;
        int INFO=1;
        int WARNING=2;
        int ERROR=3;
        int EMPHASIS=4;
        int THROW=5;
        int CATCH=6;

        int USER=1000;
    }
    public int code;
    public Object obj;
    public String clazzName;
    public String methodName;
    public String fileName;
    public int lineNumber;


    {
        clazzName= TraceUtil.getHereInvokerClass();
        methodName=TraceUtil.getHereInvokerMethod();
        fileName=TraceUtil.getHereInvokerFileName();
        lineNumber=TraceUtil.getHereInvokerLineNumber();
    }

    protected String getMsgPrefix(){
        String clsName=getClass().getSimpleName();
        int index=clsName.indexOf("Exception");
        if(index>0){
            return clsName.substring(0,index);
        }
        return clsName;
    }

    @Override
    public String getMessage() {
        return AppendUtil.strSep(":",getMsgPrefix(),super.getMessage());
    }

    public CommonsRuntimeException() {
    }

    public CommonsRuntimeException(String message) {
        super(message);
    }

    public CommonsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonsRuntimeException(Throwable cause) {
        super(cause);
    }

    public CommonsRuntimeException(int code, String message) {
        super(message);
        this.code=code;
    }

    public CommonsRuntimeException(int code, String message, Object obj) {
        super(message);
        this.code=code;
        this.obj=obj;
    }
    public CommonsRuntimeException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code=code;
    }
    public CommonsRuntimeException(int code, String message, Throwable cause, Object obj) {
        super(message, cause);
        this.code=code;
        this.obj=obj;
    }

    public CommonsRuntimeException(int code, String message, Throwable cause, Object obj, String clazzName) {
        super(message, cause);
        this.code=code;
        this.obj=obj;
        this.clazzName = clazzName;
    }

    public String getDetailMsg(){
        return  AppendUtil.buffer()
                .adds("error:", getMessage())
                .addsWhen(code != 0, "\n\t errorCode:", code)
                .addsWhen(obj != null, "\n\t errorObj:", obj)
                .addsWhen(clazzName != null, "\n\t errorClass:", clazzName)
                .addsWhen(methodName != null, "\n\t errorMethod:", methodName)
                .addsWhen(lineNumber != 0, "\n\t errorLine:", lineNumber)
                .addsWhen(fileName != null, "\n\t errorFile:", fileName)
                .done();
    }

    public static void thrCatch(String message,Throwable cause,Object obj) throws CommonsRuntimeException {
        throw new CommonsRuntimeException(ErrCode.CATCH,message,cause,obj);
    }

    public static void thrThrow(String message,Object obj) throws CommonsRuntimeException {
        throw new CommonsRuntimeException(ErrCode.THROW,message,obj);
    }

    public static CommonsRuntimeException cat(String message, Throwable cause, Object obj)  {
        return new CommonsRuntimeException(ErrCode.CATCH,message,cause,obj);
    }

    public static CommonsRuntimeException thr(String message, Object obj)  {
        return new CommonsRuntimeException(ErrCode.THROW,message,obj);
    }
}
