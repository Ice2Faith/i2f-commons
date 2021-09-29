package i2f.commons.component.json.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * @author ltb
 * @date 2021/9/7
 */
public class JsonUtil {
    private static String PROJ_DEFAULT_DATE_FMT="yyyy-MM-dd HH:mm:ss SSS";
    private static Logger logger= LoggerFactory.getLogger(JsonUtil.class);
    private static volatile ObjectMapper objectMapper;
    public static ObjectMapper getObjectMapper(){
        if(objectMapper==null){
            synchronized (JsonUtil.class){
                if(objectMapper==null){
                    objectMapper=new ObjectMapper();
                }
            }
        }
        objectMapper.setDateFormat(new SimpleDateFormat(PROJ_DEFAULT_DATE_FMT));
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return objectMapper;
    }
    public static String toJson(Object obj){
        String json=null;
        try {
            json=getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("JacksonUtil:toJson:error",e.getMessage());
        }
        return json;
    }
    public static <T> T parseJson(String json,Class<? extends T> clazz){
        T obj=null;
        try {
            obj=getObjectMapper().readValue(json,clazz);
        } catch (JsonProcessingException e) {
            logger.error("JacksonUtil:parseJson:error",e.getMessage());
        }
        return obj;
    }
    public static <T> T parseRefJson(String json, TypeReference<T> type){
        T ret=null;
        try {
            ret=getObjectMapper().readValue(json,type);
        } catch (JsonProcessingException e) {
            logger.error("JacksonUtil:parseRefJson:error",e.getMessage());
        }
        return ret;
    }
}
