package com.yff.base;

import com.yff.utils.PropertyReader;
import com.yff.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * @author YFF
 * @date 2020/4/4
 * 参考链接：
 * 1.JedisPool、JedisPoolConfig类的使用
 *      https://blog.csdn.net/u012525096/article/details/82930254
 */
@Slf4j
public class JedisBase {

    /**
     * 创建的jedisPoolConfig、jedisPool对象
     * */
    private JedisPoolConfig jedisPoolConfig;
    private JedisPool jedisPool;

    private Jedis jedis;
    private String redisIp;
    private String redisPassword;
    private int redisPort;
    private int jedisPoolMaxTotal;
    private int jedisPoolMaxIdle;
    private int jedisPoolMaxWaitMillis;
    private Boolean jedisPoolTestOnBorrow;
    private Boolean jedisPoolTestOnReturn;

    /**
     * 创建ThreadLocalUtil和ThreadLocal，用于存储Jedis对象
     * */
    private static ThreadLocalUtil<Jedis> jedisThreadLocalUtil = new ThreadLocalUtil<>();
    private static ThreadLocal<Jedis> threadJedis = new ThreadLocal<>();

    /**
     * 读取properties文件，赋值
     * */
    public void setValue(){
        this.redisIp = PropertyReader.getProperty("redis.ip");
        this.redisPort = Integer.valueOf( PropertyReader.getProperty("redis.port"));
        this.redisPassword = PropertyReader.getProperty("redis.password");
        this.jedisPoolMaxTotal = Integer.valueOf( PropertyReader.getProperty("jedis.pool.maxTotal"));
        this.jedisPoolMaxIdle = Integer.valueOf( PropertyReader.getProperty("jedis.pool.maxIdle"));
        this.jedisPoolMaxWaitMillis = Integer.valueOf( PropertyReader.getProperty("jedis.pool.maxWaitMillis"));
        this.jedisPoolTestOnBorrow = Boolean.valueOf( PropertyReader.getProperty("jedis.pool.testOnBorrow") );
        this.jedisPoolTestOnReturn = Boolean.valueOf( PropertyReader.getProperty("jedis.pool.testOnReturn") );
    }

    /**
     * 获得JedisPool对象
     * */
    public JedisPool getJedisPool() throws Exception{
        /**
         * 1 调用方法获取配置文件中数据
         * */
        setValue();
        /**
         *2 创建JedisPoolConfig对象 并设置
         * */
        jedisPoolConfig = new JedisPoolConfig();
        /*设置一个pool最多可用分配多少个jedis实例*/
        jedisPoolConfig.setMaxTotal( jedisPoolMaxTotal );
        /*设置一个pool最多能有多少个状态空闲的jedis实例*/
        jedisPoolConfig.setMaxIdle( jedisPoolMaxIdle);
        /*设置获取jedis实例的时候最大等待毫秒数*/
        jedisPoolConfig.setMaxWaitMillis( jedisPoolMaxWaitMillis );
        /*设置获取jedis实例的时候检查可用性（ping）*/
        jedisPoolConfig.setTestOnBorrow( jedisPoolTestOnBorrow );
        /*设置return jedis实例的时候检查可用性（ping）*/
        jedisPoolConfig.setTestOnReturn( jedisPoolTestOnReturn );
        /**
         * 3.使用JedisPollConfig和相关参数创建jedisPool对象并返回
         * */
        createJedisPool();
        return jedisPool;
    }

    /**
     * 创建JedisPool
     * */
    public JedisPool createJedisPool(){
        if(null==jedisPool){
            synchronized (JedisBase.class){
                if(null==jedisPool){
                    try {
                        /*创建JedisPool对象*/
                        if(this.redisPassword.equals( "" ) || this.redisPassword == null){
                            // 无需密码连接
                            jedisPool = new JedisPool(jedisPoolConfig, redisIp, redisPort ,3000);
                        }else{
                            // 需要密码连接
                            jedisPool = new JedisPool( jedisPoolConfig, redisIp, redisPort ,3000, redisPassword);
                        }
                        /**
                         *  设置ThreadLocal对象的值,通过jedisPool.getResource()获取
                         * */
                        jedisThreadLocalUtil.setThreadValue( threadJedis, jedisPool.getResource());
                        log.info("成功连接到redis服务器");
                    }catch (Exception e){
                        e.printStackTrace();
                        log.info("连接redis服务器失败");
                    }
                }
            }
        }
        return jedisPool;
    }


    /**
     * 获得jedis对象
     * */
    public Jedis getJedis() {
        return jedisThreadLocalUtil.getThreadValue( threadJedis );
    }

    /**
     * 设置jedis
     * */
    public void setJedis(Jedis jedis){
        this.jedis = jedis;
    }

    /**
     * 设置key对应的value值以及key的过期时间10min
     * */
    public String getKey(String key){
        return getJedis().get( key );
    }

    /**
     * 设置key对应的value值以及key的过期时间10min
     * */
    public void setKey(String key, String value){
        getJedis().set( key, value );
        getJedis().expire( key, 600 );
    }

    /**
     * 归还jedis对象
     * */
    public void returnJedis(){
        setJedis(getJedis());
        if (jedis != null && jedisPool != null){
            jedis.flushAll();
            jedis.close();
            log.info("成功归还jedis对象");
            threadJedis.remove();
        }
    }
}
