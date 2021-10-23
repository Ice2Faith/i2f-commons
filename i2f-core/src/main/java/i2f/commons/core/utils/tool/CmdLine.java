package i2f.commons.core.utils.tool;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2021/10/15
 */
public class CmdLine {
    public static Runtime getRuntime() {
        return Runtime.getRuntime();
    }
    public static String runLine(String cmdLine) throws IOException, InterruptedException {
        Charset charset=Charset.forName(System.getProperty("file.encoding"));
        String osName=System.getProperty("os.name").toLowerCase();
        if(osName.contains("windows")){
            Locale locale=Locale.getDefault();
            String country = locale.getCountry();//返回国家地区代码
            String language = locale.getLanguage();//返回国家的语言
            if("CN".equalsIgnoreCase(country) && "zh".equalsIgnoreCase(language)) {
                charset = Charset.forName("GBK");
            }
        }
        return runLine(cmdLine,null,null,charset);
    }
    public static String runLine(String cmdLine,   Charset charset) throws IOException, InterruptedException {
        return runLine(cmdLine,null,null,charset);
    }
    public static String runLine(String cmdLine,  File dir, Charset charset) throws IOException, InterruptedException {
        return runLine(cmdLine,null,dir,charset);
    }

    public static String execLine(String cmdLine,String[] env,File dir,Charset charset){
        String rs=null;
        try{
            rs=runLine(cmdLine, env, dir, charset);
        }catch(Exception e){

        }
        return rs;
    }

    public static String runLine(String cmdLine, String[] env, File dir, Charset charset) throws IOException, InterruptedException {
        Process process=getRuntime().exec(cmdLine, env, dir);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();

        int bt=-1;
        InputStream ois=process.getInputStream();
        while((bt= ois.read())!=-1){
            bos.write(bt);
        }
        ois.close();

        InputStream eis=process.getErrorStream();
        while((bt= eis.read())!=-1){
            bos.write(bt);
        }
        eis.close();

        process.waitFor();

        byte[] data=bos.toByteArray();
        String ret=new String(data,charset);
        return ret;
    }
    public static Process run(String cmdLine) throws IOException, InterruptedException {
        return run(cmdLine,null,null,false,-1,null);
    }
    public static Process run(String cmdLine,  File dir) throws IOException, InterruptedException {
        return run(cmdLine,null,dir,false,-1,null);
    }
    public static Process run(String cmdLine, String[] env, File dir) throws IOException, InterruptedException {
        return run(cmdLine, env, dir,false,-1,null);
    }
    public static Process run(String cmdLine, String[] env, File dir, boolean wait) throws IOException, InterruptedException {
        return run(cmdLine, env, dir, wait,-1,null);
    }

    public static Process exec(String cmdLine,String[] env,File dir,boolean wait,long timeout,TimeUnit timeUnit){
        Process process=null;
        try{
            process=run(cmdLine, env, dir, wait, timeout, timeUnit);
        }catch(Exception e){

        }
        return process;
    }

    public static Process run(String cmdLine, String[] env, File dir, boolean wait, long timeout, TimeUnit timeUnit) throws IOException, InterruptedException {
        Process process = getRuntime().exec(cmdLine, env, dir);
        if (wait) {
            if(timeout>0){
                process.waitFor(timeout,timeUnit);
            }else{
                process.waitFor();
            }
        }
        return process;
    }
}
