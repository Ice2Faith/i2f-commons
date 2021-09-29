package i2f.commons.component.spring.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author ltb
 * @date 2021/8/10
 */
public class JsonUtil {
    private static volatile ObjectMapper mapper;
    private JsonUtil(){

    }
    public static ObjectMapper getMapper(){
        if(mapper==null){
            synchronized (JsonUtil.class){
                if(mapper==null){
                    mapper=new ObjectMapper();

                }
            }
        }
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
    public static String toJson(Object obj) throws JsonProcessingException {
        return getMapper().writeValueAsString(obj);
    }
    public static<T> T parseObj(String json,Class<T> clazz) throws JsonProcessingException {
        return getMapper().readValue(json,clazz);
    }
    public static<T> T parseRef(String json, TypeReference<T> ref) throws JsonProcessingException {
        return getMapper().readValue(json,ref);
    }
    public static Map<String,Object> bean2Map(Object bean) throws JsonProcessingException {
        String json=toJson(bean);
        Map<String, Object> map=parseRef(json, new TypeReference<Map<String, Object>>() {});
        return map;
    }
    public static<T> T map2Bean(Map<String,Object> map,Class<T> clazz) throws JsonProcessingException {
        String json=toJson(map);
        T bean=parseObj(json,clazz);
        return bean;
    }
}
