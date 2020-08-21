package com.yundingshuyuan.website.util;

import com.yundingshuyuan.website.enums.RedisPrefix;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author leeyf
 * redis管理类
 */
@Component
public class RedisManager {



    @Resource
    private RedisTemplate redisTemplate;


    /**
     * set
     *存
     * @param key
     * @param value
     * @param prefix 前缀
     */
    public void setValue(RedisPrefix prefix, String key, Object value) {

        long expird = prefix.getExpire();

        if (expird == -1){
            redisTemplate.opsForValue().set(this.realKey(prefix,key), value);
        } else {
            redisTemplate.opsForValue().set(this.realKey(prefix,key), value, expird, prefix.getTimeUnit());
        }

    }

    public void setValueIncrease(String key) {
        redisTemplate.opsForValue().increment(key, 1);
    }
    /**
     * 根据key获取value
     * @param key key
     * @return 返回value
     * @author leeyf
     */
    public <T> T getValue(RedisPrefix prefix, String key) {

        return (T) redisTemplate.opsForValue().get(this.realKey(prefix,key));

    }


    public void setMap(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    public List<Map<String, Object>> getMap(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 元素是否存在于set中
     *
     * @param prefix 前缀
     * @param key 键
     * @param member 元素
     * @return true/false
     */
    public boolean isMemberInSet(RedisPrefix prefix, String key, Object member){

        return redisTemplate.opsForSet().isMember(realKey(prefix, key), member);

    }

    /**
     * 向set中添加元素
     * @param prefix
     * @param key
     * @param member
     */
    public void addMemberToSet(RedisPrefix prefix, String key, Object member) {

        redisTemplate.opsForSet().add(realKey(prefix, key), member);

    }

    public Long countSet(RedisPrefix prefix, String key){

        return redisTemplate.opsForSet().size(realKey(prefix, key));

    }


    public boolean hasKey(RedisPrefix prefix, String key) {
        return redisTemplate.hasKey(this.realKey(prefix,key));
    }

    public void delete(RedisPrefix prefix, String key) {
        redisTemplate.delete(this.realKey(prefix,key));
    }





    /**
     * 延长key的过期时间
     * @param key
     * @param prefix 前缀
     */
    public void expire(RedisPrefix prefix, String key){

        long expird = prefix.getExpire();

        redisTemplate.expire(this.realKey(prefix,key),expird, prefix.getTimeUnit());

    }


    /**
     * 延长key的过期时间
     * @param key
     * @param time 单位: s 秒
     */
    public void expire(String key, long time, TimeUnit timeUnit){

        redisTemplate.expire(key,time, timeUnit);

    }

    /**
     * 根据缓存前缀匹配删除缓存
     * @param prefix 缓存前缀
     * @author leeyf
     */
    public void deleteBatch(String prefix, String key) {
        Set<String> keys = redisTemplate.keys(prefix + "*" + key);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }


    private String realKey(RedisPrefix prefix,String key){
        return prefix.getPrefix() + key;
    }




}