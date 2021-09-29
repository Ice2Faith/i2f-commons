package i2f.commons.core.data.web.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespData {
    public interface Code{
        Integer SUCCESS=200;
        Integer ERROR=0;
        Integer NO_LOGIN=400;
        Integer NO_AUTH=401;
        Integer UNKNOWN=402;
        Integer NOT_FOUND=404;
        Integer SYS_EXCEPTION=500;
    }
    public Integer code;
    public String msg;
    public Object data;
    public Object tag;

    public RespData(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RespData(Integer code, String msg, Object data, Object tag) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.tag = tag;
    }
    public static RespData success(Object data){
        return new RespData(Code.SUCCESS,"success",data,null);
    }
    public static RespData error(String msg){
        return new RespData(Code.ERROR,msg,null,null);
    }
    public static RespData success(Object data, String msg){
        return new RespData(Code.SUCCESS,msg,data,null);
    }
    public static RespData error(String msg, Object data){
        return new RespData(Code.ERROR,msg,data,null);
    }
    public static RespData exception(String msg, Object data){
        return new RespData(Code.SYS_EXCEPTION,msg,data,null);
    }
    public static RespData notLogin(String msg, Object data){
        return new RespData(Code.NO_LOGIN,msg,data,null);
    }
    public static RespData notAuth(String msg, Object data){
        return new RespData(Code.NO_AUTH,msg,data,null);
    }

    public static RespData success(Object data, String msg, Object tag){
        return new RespData(Code.SUCCESS,msg,data,tag);
    }
    public static RespData error(String msg, Object data, Object tag){
        return new RespData(Code.ERROR,msg,data,tag);
    }
    public static RespData exception(String msg, Object data, Object tag){
        return new RespData(Code.SYS_EXCEPTION,msg,data,tag);
    }
    public static RespData notLogin(String msg, Object data, Object tag){
        return new RespData(Code.NO_LOGIN,msg,data,tag);
    }
    public static RespData notAuth(String msg, Object data, Object tag){
        return new RespData(Code.NO_AUTH,msg,data,tag);
    }
}
