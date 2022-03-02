package i2f.log.test;

import i2f.log.Environment;
import i2f.log.api.LogWriter;
import i2f.log.api.impl.FileLogWriterImpl;
import i2f.log.api.impl.MulticastLogWriter;
import i2f.log.api.impl.PrintStreamLogWriterImpl;
import i2f.log.api.impl.StdoutLogWriterImpl;
import i2f.log.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ltb
 * @date 2022/3/2 15:54
 * @desc
 */
public class LogTest {
    public static void main(String[] args){
        System.setProperty(Environment.REGISTER_UNIQUE_WRITER,"false");
        BaseLogModel log=new BaseLogModel();
        MulticastLogWriter manager=new MulticastLogWriter();
        manager.registerLogWriter(new StdoutLogWriterImpl());
        manager.registerLogWriter(new PrintStreamLogWriterImpl(System.err));
        manager.registerLogWriter(new FileLogWriterImpl("./logs/LogTest.log"));
        manager.registerLogWriter(new FileLogWriterImpl("./logs/LogTestDispach.log"));
        LogWriter writer=manager;


        log.setTime(new Date());
        log.setLevel("INFO");
        log.setType("NORMAL");
        log.setLocation(LogTest.class.getName());
        log.setContent("test base log");
        writer.write(log);

        BaseLocationLogModel loc=new BaseLocationLogModel();
        loc.setTime(new Date());
        loc.setLevel("INFO");
        loc.setType("NORMAL");
        loc.setLocation(LogTest.class.getName());
        loc.setContent("test base log");

        loc.setClassName(LogTest.class.getName());
        loc.setMethod("main");
        loc.setThread(Thread.currentThread().getName());
        loc.setFileName("LogTest.java");
        loc.setLine("38");
        writer.write(loc);

        manager.unregisterLogWriter("printStreamLogWriterImpl");
        SimpleBaseLogModel smp=new SimpleBaseLogModel();
        smp.setTime(new Date());
        smp.setLevel("INFO");
        smp.setType("NORMAL");
        smp.setLocation(LogTest.class.getName());
        smp.setContent("test base log");

        smp.setTime(new Date());
        smp.setLevel("INFO");
        smp.setType("NORMAL");
        smp.setLocation(LogTest.class.getName());
        smp.setContent("test base log");

        smp.setClassName(LogTest.class.getName());
        smp.setMethod("main");
        smp.setThread(Thread.currentThread().getName());
        smp.setFileName("LogTest.java");
        smp.setLine("38");

        smp.setSystem("疫情网格化");
        smp.setModule("自主申报");
        smp.setLabel("变更状态");
        writer.write(smp);

        ExceptionLogModel ex=new ExceptionLogModel();
        ex.setTime(new Date());
        ex.setLevel("INFO");
        ex.setType("NORMAL");
        ex.setLocation(LogTest.class.getName());
        ex.setContent("test base log");

        ex.setTime(new Date());
        ex.setLevel("INFO");
        ex.setType("NORMAL");
        ex.setLocation(LogTest.class.getName());
        ex.setContent("test base log");

        ex.setClassName(LogTest.class.getName());
        ex.setMethod("main");
        ex.setThread(Thread.currentThread().getName());
        ex.setFileName("LogTest.java");
        ex.setLine("38");

        ex.setSystem("疫情网格化");
        ex.setModule("自主申报");
        ex.setLabel("变更状态");

        ex.setExceptionClassName(RuntimeException.class.getName());
        ex.setExceptionMessage("division by zero");
        writer.write(ex);

        InvocationLogModel ivk=new InvocationLogModel();
        ivk.setTime(new Date());
        ivk.setLevel("INFO");
        ivk.setType("NORMAL");
        ivk.setLocation(LogTest.class.getName());
        ivk.setContent("test base log");

        ivk.setTime(new Date());
        ivk.setLevel("INFO");
        ivk.setType("NORMAL");
        ivk.setLocation(LogTest.class.getName());
        ivk.setContent("test base log");

        ivk.setClassName(LogTest.class.getName());
        ivk.setMethod("main");
        ivk.setThread(Thread.currentThread().getName());
        ivk.setFileName("LogTest.java");
        ivk.setLine("38");

        ivk.setSystem("疫情网格化");
        ivk.setModule("自主申报");
        ivk.setLabel("变更状态");

        ivk.setExceptionClassName(RuntimeException.class.getName());
        ivk.setExceptionMessage("division by zero");

        ivk.setInvokeType("AROUND");
        ivk.setInvokeArgs("{userName:zhang}");
        ivk.setReturnValue("{code:200,msg:ok,data:null}");
        ivk.setBeginDate(new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date()));
        ivk.setEndDate(new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date()));

        writer.write(ivk);


        RemoteInvokeLogModel rmt=new RemoteInvokeLogModel();
        rmt.setTime(new Date());
        rmt.setLevel("INFO");
        rmt.setType("NORMAL");
        rmt.setLocation(LogTest.class.getName());
        rmt.setContent("test base log");

        rmt.setTime(new Date());
        rmt.setLevel("INFO");
        rmt.setType("NORMAL");
        rmt.setLocation(LogTest.class.getName());
        rmt.setContent("test base log");

        rmt.setClassName(LogTest.class.getName());
        rmt.setMethod("main");
        rmt.setThread(Thread.currentThread().getName());
        rmt.setFileName("LogTest.java");
        rmt.setLine("38");

        rmt.setSystem("疫情网格化");
        rmt.setModule("自主申报");
        rmt.setLabel("变更状态");

        rmt.setExceptionClassName(RuntimeException.class.getName());
        rmt.setExceptionMessage("division by zero");

        rmt.setInvokeType("AROUND");
        rmt.setInvokeArgs("{userName:zhang}");
        rmt.setReturnValue("{code:200,msg:ok,data:null}");
        rmt.setBeginDate(new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date()));
        rmt.setEndDate(new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date()));

        rmt.setRequestUrl("http://localhost:9527/auth");
        rmt.setRequestDate(new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date()));
        rmt.setResponseDate(new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date()));
        rmt.setRequestMethod("POST");
        rmt.setRequestContent("{username:zhang,password:123456}");
        rmt.setResponseContent("{code:200,data:0x55224}");
        writer.write(rmt);
    }
}
