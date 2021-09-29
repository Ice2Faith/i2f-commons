package i2f.commons.component.redis.inject.pool;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.SocketTimeoutException;
import java.util.*;

//for ssm
// use : extends this class and add @Component annotation
public class RedisPoolUtil {

    @Autowired
    protected JedisPool pool;

    synchronized public Jedis getJedis(){
        int timeoutCount=0;
        Jedis jedis=null;
        while(true){
            try{
                jedis=pool.getResource();
                break;
            }catch(Exception e){
                // 底层原因是SocketTimeoutException，不过redis已经捕捉且抛出JedisConnectionException，不继承于前者
                if (e instanceof JedisConnectionException || e instanceof SocketTimeoutException)
                {
                    timeoutCount++;
                    if (timeoutCount > 3)
                    {
                        break;
                    }
                }
                else
                {
                    break;
                }
            }

        }
        return jedis;
    }

    public  boolean clearRedis(){
        Jedis jedis=getJedis();
        if(jedis==null){
            return false;
        }
        jedis.flushDB();
        returnJedis(jedis);
        return true;
    }
    public  void returnJedis(Jedis jedis){
        if(jedis!=null){
            pool.returnResource(jedis);
        }
    }

    public  boolean hasKey(String key){
        Jedis jedis=getJedis();
        if(jedis==null){
            return false;
        }
        boolean ret=jedis.exists(key);
        returnJedis(jedis);
        return ret;
    }
    public  boolean set(String key,String val){
        return set(key,val,0);
    }
    public  boolean set(String key,String val,int timeOutSecond){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return false;
        }
        jedis.set(key,val);
        if(timeOutSecond>0){
            jedis.expire(key,timeOutSecond);
        }
        returnJedis(jedis);
        return true;
    }

    public  boolean updateKeyTimeout(String key,int timeOutSecond){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return false;
        }
        boolean ret=false;
        if(jedis.exists(key)){
            jedis.expire(key,timeOutSecond);
            ret=true;
        }
        returnJedis(jedis);
        return true;
    }

    public  boolean setUnique(String key,String val,int timeOutSecond){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return false;
        }

        boolean ret=false;
        if(jedis.exists(key)) {
            ret=false;
        } else if("OK".equals(jedis.set(key,val,"NX","EX",timeOutSecond))){
            ret=true;
        }else{
            ret=false;
        }

        returnJedis(jedis);
        return ret;
    }

    public  String del(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=null;
        if(jedis.exists(key)){
            ret=jedis.get(key);
            jedis.del(key);
        }
        returnJedis(jedis);
        return ret;
    }
    public  String get(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=null;
        if(jedis.exists(key)){
            ret=jedis.get(key);
        }
        returnJedis(jedis);
        return ret;
    }
    public Set<String> keys(String patten){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        Set<String> ret=null;
        ret=jedis.keys(patten);
        returnJedis(jedis);
        return ret;
    }

    /**
     * Redis的List支持，也就是一个键key下面存放的是一个List
     * @param key  Redis键
     * @param values List的变长值
     * @return 个数
     */
    public  long listPush(String key,String ... values){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.lpush(key,values);
        returnJedis(jedis);
        return ret;
    }
    public  long listLength(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.llen(key);
        returnJedis(jedis);
        return ret;
    }
    public List<String> listAll(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return new ArrayList<>();
        }
        List<String> ret=jedis.lrange(key,0,jedis.llen(key));
        returnJedis(jedis);
        return ret;
    }
    public  String listAt(String key,long index){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=jedis.lindex(key,index);
        returnJedis(jedis);
        return ret;
    }
    public  String listSet(String key,long index,String value){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=jedis.lset(key,index,value);
        returnJedis(jedis);
        return ret;
    }

    /**
     * Redis的Hash支持
     * 也就是某一个键key下面存放的是一个Hash域值对(Field,Value)
     * @param key Redis键
     * @param field Hash域
     * @param value Hash
     * @return 返回个数
     */
    public  long hashSet(String key,String field,String value){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.hset(key,field,value);
        returnJedis(jedis);
        return ret;
    }
    public  String hashGet(String key,String field){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=jedis.hget(key,field);
        returnJedis(jedis);
        return ret;
    }
    public  String hashSetMap(String key, Map<String,String> map){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=jedis.hmset(key,map);
        returnJedis(jedis);
        return ret;
    }
    public  Map<String,String> hashGetAll(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return new HashMap<>();
        }
        Map<String,String> ret=jedis.hgetAll(key);
        returnJedis(jedis);
        return ret;
    }
    public  long hashSize(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.hlen(key);
        returnJedis(jedis);
        return ret;
    }
    public  long hashDelete(String key,String ... fields){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.hdel(key,fields);
        returnJedis(jedis);
        return ret;
    }
}
