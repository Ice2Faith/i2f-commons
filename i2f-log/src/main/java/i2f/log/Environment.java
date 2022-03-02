package i2f.log;

/**
 * @author ltb
 * @date 2022/3/2 16:40
 * @desc
 */
public class Environment {
    public static final String REGISTER_UNIQUE_WRITER="i2f.log.register.unique-writer";
    public static boolean registerUniqueWriter=true;


    static {
        refreshEnvironment();
    }

    public static void refreshEnvironment(){
        registerUniqueWriter=getBooleanProperty(REGISTER_UNIQUE_WRITER,true);
    }

    public static boolean getBooleanProperty(String key,boolean defVal){
        String pro=System.getProperty(key);
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
