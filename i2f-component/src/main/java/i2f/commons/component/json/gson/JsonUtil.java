package i2f.commons.component.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
    public static String dateFormatPatten="yyyy-MM-dd HH:mm:ss SSS";

    public static Gson getGson(){
        Gson gson=new GsonBuilder()
                .setDateFormat(dateFormatPatten)
                .create();
        return gson;
    }
    /**
     * 将对象转换为Json串
     * 用法：
     * String js=toJson(new Admin());
     * @param obj 对象
     * @param <T> 对象类型
     * @return Json串
     */
    public static<T>String toJson(T obj){
        Gson gson=getGson();
        return gson.toJson(obj);
    }

    /**
     * 将一个Json串解析为对象
     * 用法：
     * Admin admin=fromJson(js,Admin.class);
     * @param json Json串
     * @param clazz 类类型
     * @param <T> 类型
     * @return 类对象
     */
    public static<T>T fromJson(String json,Class<T>clazz){
        Gson gson=getGson();
        return gson.fromJson(json,clazz);
    }


    /**
     * 将一个Json串解析为对象集合
     * 用法：
     * Lis<Admin> list=fromJsonArray(js);
     * @param json Json串
     * @param <T> 类型
     * @return 对象集合
     */
    public static<T>T fromJsonTypeToken(String json){
        Gson gson=getGson();
        return gson.fromJson(json,new TypeToken<T>(){}.getType());
    }

    public static JsonObject formJson(String json){
        JsonParser parser=new JsonParser();
        return parser.parse(json).getAsJsonObject();
    }
}
