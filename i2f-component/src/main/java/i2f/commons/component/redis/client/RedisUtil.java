package i2f.commons.component.redis.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.SocketTimeoutException;
import java.util.*;

public class RedisUtil {

    public static final int MAX_TOTAL=1024;
    public static final int MAX_IDLE=256;
    public static final int MAX_WAIT_MILLIS=50*1000;

    public static final boolean TEST_ON_BORROW =true;
    public static final boolean TEST_ON_RETURN=true;
    public static final boolean  TEST_WHILE_IDLE=true;

    public static final int TIME_BETWEEN_EVICTION_RUNS_MILLIS=1000 * 120;

    public static final int POOL_TIMEOUT=50*1000;
    private static JedisPool pool=null;

    private static boolean hadInitialed=false;

    /**
     * 使用默认端口号6379进行初始化
     * @param host
     * @param auth
     * @return
     */
    public static boolean initial(String host,String auth){
        return initial(host,6379,auth);
    }
    /**
     * 使用本工具之前，需要调用此方法进行初始化,只会执行一次
     * 不过，如果你多次调用也不会有什么影响
     * 如果初始化失败，将会报运行时异常
     * @param host 主机地址
     * @param port 主机端口
     * @param auth Redis密码
     * @return 是否成功
     */
    public static boolean initial(String host,int port,String auth){
        if(hadInitialed){
            return true;
        }
        try {
            //单机版

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT_MILLIS);
            config.setTestOnBorrow(TEST_ON_BORROW);
            config.setTestOnReturn(TEST_ON_RETURN);
            config.setTestWhileIdle(TEST_WHILE_IDLE);
            config.setTimeBetweenEvictionRunsMillis(TIME_BETWEEN_EVICTION_RUNS_MILLIS);

            pool = new JedisPool(config, host, port,POOL_TIMEOUT, auth);

            hadInitialed=true;
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    synchronized public static Jedis getJedis(){
        if(pool==null){
            throw new RuntimeException("RedisClientUtil's Initial() method must be invoked before use it.");
        }
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

    public static boolean clearRedis(){
        Jedis jedis=getJedis();
        if(jedis==null){
            return false;
        }
        jedis.flushDB();
        returnJedis(jedis);
        return true;
    }
    public static void returnJedis(Jedis jedis){
        if(jedis!=null){
            pool.returnResource(jedis);
        }
    }

    public static boolean hasKey(String key){
        Jedis jedis=getJedis();
        if(jedis==null){
            return false;
        }
        boolean ret=jedis.exists(key);
        returnJedis(jedis);
        return ret;
    }
    public static boolean set(String key,String val){
        return set(key,val,0);
    }
    public static boolean set(String key,String val,int timeOutSecond){
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

    public static boolean updateKeyTimeout(String key,int timeOutSecond){
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

    public static boolean setUnique(String key,String val,int timeOutSecond){
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

    public static String del(String key){
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
    public static String get(String key){
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
    public static Set<String> keys(String patten){
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
    public static long listPush(String key,String ... values){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.lpush(key,values);
        returnJedis(jedis);
        return ret;
    }
    public static long listLength(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.llen(key);
        returnJedis(jedis);
        return ret;
    }
    public static List<String> listAll(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return new ArrayList<>();
        }
        List<String> ret=jedis.lrange(key,0,jedis.llen(key));
        returnJedis(jedis);
        return ret;
    }
    public static String listAt(String key,long index){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=jedis.lindex(key,index);
        returnJedis(jedis);
        return ret;
    }
    public static String listSet(String key,long index,String value){
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
    public static long hashSet(String key,String field,String value){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.hset(key,field,value);
        returnJedis(jedis);
        return ret;
    }
    public static String hashGet(String key,String field){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=jedis.hget(key,field);
        returnJedis(jedis);
        return ret;
    }
    public static String hashSetMap(String key, Map<String,String> map){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return null;
        }
        String ret=jedis.hmset(key,map);
        returnJedis(jedis);
        return ret;
    }
    public static Map<String,String> hashGetAll(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return new HashMap<>();
        }
        Map<String,String> ret=jedis.hgetAll(key);
        returnJedis(jedis);
        return ret;
    }
    public static long hashSize(String key){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.hlen(key);
        returnJedis(jedis);
        return ret;
    }
    public static long hashDelete(String key,String ... fields){
        Jedis jedis=getJedis();
        if(jedis==null) {
            return 0;
        }
        Long ret=jedis.hdel(key,fields);
        returnJedis(jedis);
        return ret;
    }
}
