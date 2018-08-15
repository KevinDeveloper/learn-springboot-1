package com.kevin.springbootone.testRedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisService
 * @Description: redis 常规操作 简单封装
 * @Author: Kevin
 * @Date: 2018/4/26 09:30
 */
@Service
public class RedisCacheUtil {

    /**
     * redis 操作
     * <p>
     * redisTemplate.opsForValue();//操作字符串
     * redisTemplate.opsForHash();//操作hash
     * redisTemplate.opsForList();//操作list
     * redisTemplate.opsForSet();//操作set
     * redisTemplate.opsForZSet();//操作有序set
     */
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 操作String数据类型
     * StringRedisTemplate是RedisTemplate的唯一子类。
     */
    @Autowired
    public StringRedisTemplate stringRedisTemplate;


    //--------------------------------------------------------string

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        return operation;
    }

    public <T> ValueOperations<String, T> setCacheObject(String key, T value, long expireTime, TimeUnit unit) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value, expireTime, unit);
        return operation;
    }

    public <T> ValueOperations<String, T> incrementCacheObject(String key, long value, long expireTime, TimeUnit unit) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.increment(key, value);
        redisTemplate.expire(key, expireTime, unit);
        return operation;
    }

    public ValueOperations<String, Object> multiSetCacheObject(Map<String, Object> map, long expireTime, TimeUnit unit) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        operation.multiSet(map);
        for (String key : map.keySet()) {
            redisTemplate.expire(key, expireTime, unit);
        }
        return operation;
    }

    public List<Object> multiGetCacheObject(List<String> keyList) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        List<Object> val = operation.multiGet(keyList);
        return val;
    }


    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }


    //--------------------------------------------list

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            listOperation.leftPushAll(key, dataList);
        }
        return listOperation;
    }

    /**
     * 该方法需要在事务中执行，由于push 和 expire是两条语句 ，会发起两次redis请求
     *
     * @param key
     * @param dataList
     * @param expireTime
     * @param unit
     * @param <T>
     * @return
     */
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList, long expireTime, TimeUnit unit) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            dataList = new ArrayList<>();
            listOperation.leftPushAll(key, dataList);
            redisTemplate.expire(key, expireTime, unit);
        }
        return listOperation;
    }

    public <T> ListOperations<String, T> setCacheListReset(String key, List<T> dataList, long expireTime, TimeUnit unit) {
        if (exists(key)) {
            delete(key);
        }
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            listOperation.leftPushAll(key, dataList);
            redisTemplate.expire(key, expireTime, unit);
        }
        return listOperation;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(String key) {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = redisTemplate.opsForList();
        long size = listOperation.size(key);
        dataList = listOperation.range(key, 1, size - 1);
        return dataList;
    }

    //------------------------------------------------------ set

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public void setCacheSet(String key, Set dataSet) {
        SetOperations setOperation = redisTemplate.opsForSet();
        if (null != dataSet) {
            setOperation.add(key, dataSet.toArray());
        }
    }

    public void setCacheSet(String key, Set dataSet, long expireTime, TimeUnit unit) {
        SetOperations setOperation = redisTemplate.opsForSet();
        if (null != dataSet) {
            setOperation.add(key, dataSet.toArray());
            redisTemplate.expire(key, expireTime, unit);
        }

    }


    public void addCacheSet(String key, Object object) {
        SetOperations setOperation = redisTemplate.opsForSet();
        setOperation.add(key, object);
    }

    public void addCacheSet(String key, Object object, long expireTime, TimeUnit unit) {
        SetOperations setOperation = redisTemplate.opsForSet();
        setOperation.add(key, object);
        redisTemplate.expire(key, expireTime, unit);
    }


    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(String key) {
        Set<T> dataSet = new HashSet<T>();
        SetOperations setOperation = redisTemplate.opsForSet();
        dataSet = setOperation.members(key);
        return dataSet;
    }

    public void removeCacheSet(String key, Object... values) {
        SetOperations setOperation = redisTemplate.opsForSet();
        setOperation.remove(key, values);
    }

    //------------------------------------------------map

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public void setCacheMap(String key, Map dataMap) {
        HashOperations hashOperation = redisTemplate.opsForHash();
        if (null != dataMap) {
            hashOperation.putAll(key, dataMap);
        }
    }

    public void setCacheMap(String key, Map dataMap, long expireTime, TimeUnit unit) {
        HashOperations hashOperation = redisTemplate.opsForHash();
        if (null != dataMap) {
            hashOperation.putAll(key, dataMap);
            redisTemplate.expire(key, expireTime, unit);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(String key) {
        Map<String, T> map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    //---------------------------------------------------

    /**
     * 删除指定key的value
     *
     * @param key
     */
    public void delete(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }


    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

}
