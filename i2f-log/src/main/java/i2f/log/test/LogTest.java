package i2f.log.test;

import i2f.log.Environment;
import i2f.log.LogContext;
import i2f.log.annotations.LogEntry;
import i2f.log.annotations.LogParam;
import i2f.log.api.impl.FileLogWriterImpl;
import i2f.log.api.impl.PrintStreamLogWriterImpl;
import i2f.log.api.impl.StdoutLogWriterImpl;
import i2f.log.enums.InvocationRecord;
import i2f.log.enums.InvocationType;
import i2f.log.enums.LogLevel;
import i2f.log.impl.SimpleLogger;
import i2f.log.model.RemoteInvokeLogModel;
import i2f.log.resolver.LogResolver;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/2 15:54
 * @desc
 */
public class LogTest {
    public static void main(String[] args) throws NoSuchMethodException {
        System.setProperty(Environment.REGISTER_UNIQUE_WRITER,"false");
        Environment.projectDefaultLogLevel=LogLevel.INFO;
        LogContext.decision.registerRule("i2f\\.log\\.test\\..+",LogLevel.INFO);
        LogContext.writer.registerLogWriter(new StdoutLogWriterImpl());
        LogContext.writer.registerLogWriter(new PrintStreamLogWriterImpl(System.err));
        LogContext.writer.registerLogWriter(new FileLogWriterImpl("./logs/LogTest.log"));
        LogContext.writer.registerLogWriter(new FileLogWriterImpl("./logs/LogTestDispach.log"));

        RemoteInvokeLogModel log=new RemoteInvokeLogModel();
        SimpleLogger logger=new SimpleLogger(LogTest.class.getName(),"日志系统","日志测试");

        logger.label("简易测试性质日志").type("框架测试");

        LogResolver resolver=new LogResolver();
        resolver.resolve(log,"测试性质日志", LogLevel.INFO,"test invoke");
        resolver.resolve(log);
        resolver.resolve(log,new RuntimeException("division by zero"));

        logger.debug("prepare do log test...");
        Class clazz=LogTest.class;
        Method method=clazz.getDeclaredMethod("test",String.class,int.class);

        Object[] ivkArgs=new Object[2];
        ivkArgs[0]="Zhang";
        ivkArgs[1]=22;
        try{
            resolver.resolve(log,method,ivkArgs,null,null);
            logger.info(log);
            Object retVal = method.invoke(null,ivkArgs);
            resolver.resolve(log,method,ivkArgs,retVal,null);
            logger.debug(log);
        }catch(Exception e){
            resolver.resolve(log,method,ivkArgs,null,e);
            logger.warn(log);
        }

        logger.info(log);
    }

    @LogEntry(system = "日志系统",module = "日志解析",label = "反射测试",
            entries = {InvocationType.AROUND,InvocationType.RETURN,InvocationType.EXCEPTION},
            records = {InvocationRecord.ARGUMENT,InvocationRecord.EXCEPTION,InvocationRecord.RETURN})
    private static String test(@LogParam("userName") String name, @LogParam("userAge") int age){
        return "name:"+name+",age:"+age;
    }
}
