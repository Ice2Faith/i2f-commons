package i2f.commons.component.redis.inject.template;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Set;

// for springboot
// use : extends this class and add @Component annotation
public class RedisTemplateUtil {
    @Resource
    protected RedisTemplate<String,String> redisTemplate;


    public  boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }
    public  boolean set(String key,String val){
        return set(key,val,0);
    }
    public  boolean set(String key,String val,int timeOutSecond){
        redisTemplate.opsForValue().set(key,val);
        if(timeOutSecond>0){
            redisTemplate.expire(key, Duration.ofSeconds(timeOutSecond));
        }
        return true;
    }

    public  boolean updateKeyTimeout(String key,int timeOutSecond){
        boolean ret=false;
        if(redisTemplate.hasKey(key)){
            redisTemplate.expire(key,Duration.ofSeconds(timeOutSecond));
            ret=true;
        }
        return true;
    }

    public  boolean setUnique(String key,String val,int timeOutSecond){
        boolean ret=false;
        if(redisTemplate.hasKey(key)) {
            ret = false;
        }else{
            redisTemplate.opsForValue().set(key,val,Duration.ofSeconds(timeOutSecond));
            ret=true;
        }
        return ret;
    }

    public  String del(String key){
        String ret=null;
        if(redisTemplate.hasKey(key)){
            ret=redisTemplate.opsForValue().get(key);
            redisTemplate.delete(key);
        }
        return ret;
    }
    public  String get(String key){
        String ret=null;
        if(redisTemplate.hasKey(key)){
            ret=redisTemplate.opsForValue().get(key);
        }
        return ret;
    }
    public Set<String> keys(String patten){
        Set<String> ret=null;
        ret=redisTemplate.keys(patten);
        return ret;
    }
    /**
     * Redis的List支持，也就是一个键key下面存放的是一个List
     * @param key  Redis键
     * @param values List的变长值
     * @return 个数
     */
    public  long listPush(String key,String ... values){
        Long ret=redisTemplate.opsForList().rightPushAll(key,values);
        return ret;
    }
    public  long listLength(String key){
        Long ret=redisTemplate.opsForList().size(key);
        return ret;
    }
    public List<String> listAll(String key){
        List<String> ret=redisTemplate.opsForList().range(key,0,redisTemplate.opsForList().size(key));
        return ret;
    }
    public  String listAt(String key,long index){
        String ret=redisTemplate.opsForList().index(key,index);
        return ret;
    }
    public  String listSet(String key,long index,String value){
        String ret=redisTemplate.opsForList().index(key,index);
        redisTemplate.opsForList().set(key,index,value);
        return ret;
    }
}
