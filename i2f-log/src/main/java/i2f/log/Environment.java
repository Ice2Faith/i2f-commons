package i2f.log;

import i2f.log.enums.LogLevel;

/**
 * @author ltb
 * @date 2022/3/2 16:40
 * @desc
 */
public class Environment {
    public static final String REGISTER_UNIQUE_WRITER="i2f.log.register.unique-writer";
    public static boolean registerUniqueWriter=true;

    public static final String DEFAULT_LOG_LEVEL="i2f.log.default-level";
    public static LogLevel projectDefaultLogLevel=LogLevel.WARN;

    static {
        refreshEnvironment();
    }

    public static void refreshEnvironment(){
        registerUniqueWriter=getBooleanProperty(REGISTER_UNIQUE_WRITER,true);

        String sLevel=getProperty(DEFAULT_LOG_LEVEL,"WARN");
        LogLevel lvl=LogLevel.parse(sLevel);
        if(lvl==null){
            lvl=LogLevel.WARN;
        }
        projectDefaultLogLevel=lvl;

    }

    public static double getDoubleProperty(String key,double defVal){
        String pro=getProperty(key,null);
        if(pro==null){
            return defVal;
        }
        try{
            return Double.parseDouble(pro);
        }catch(Exception e){
        }
        return defVal;
    }

    public static float getFloatProperty(String key,float defVal){
        String pro=getProperty(key,null);
        if(pro==null){
            return defVal;
        }
        try{
            return Float.parseFloat(pro);
        }catch(Exception e){
        }
        return defVal;
    }

    public static long getLongProperty(String key,long defVal){
        String pro=getProperty(key,null);
        if(pro==null){
            return defVal;
        }
        try{
            return Long.parseLong(pro);
        }catch(Exception e){
        }
        return defVal;
    }

    public static int getIntegerProperty(String key,int defVal){
        String pro=getProperty(key,null);
        if(pro==null){
            return defVal;
        }
        try{
            return Integer.parseInt(pro);
        }catch(Exception e){
        }
        return defVal;
    }

    public static String getProperty(String key,String defVal){
        String pro=System.getProperty(key);
        if(pro==null){
            return defVal;
        }
        return pro;
    }

    public static boolean getBooleanProperty(String key,boolean defVal){
        String pro=getProperty(key,null);
        if(pro==null){
            return defVal;
        }
        try{
            return Boolean.parseBoolean(pro);
        }catch(Exception e){
        }
        return defVal;
    }
}
