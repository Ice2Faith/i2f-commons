package i2f.commons.core.utils.jdbc.generate.data;

/**
 * @author ltb
 * @date 2021/9/28
 */
public enum DbGenType {
    BEAN(1),
    DAO(2),
    MAPPER(4),
    SERVICE(8),
    SERVICE_IMPL(16),
    CONTROLLER(32),
    AXIOS_REQUEST(64),
    ALL(0xffffff);

    private int code;
    private DbGenType(int code){
        this.code=code;
    }
    public int getCode(){
        return code;
    }
    public static int mask(DbGenType ... types){
        int code=0;
        for(DbGenType item : types){
            code|=item.getCode();
        }
        return code;
    }
    public static boolean isType(int code,DbGenType type){
        return (code& type.getCode())!=0;
    }

}
