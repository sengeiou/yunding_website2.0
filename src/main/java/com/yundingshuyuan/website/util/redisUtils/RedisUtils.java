/**
 * FileName:RedisUtils
 * Author:leeyf
 * Date:19-1-20 下午7:08
 * Description:redis工具类
 */
package com.yundingshuyuan.website.util.redisUtils;

import com.yundingshuyuan.website.constants.SysConstant;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisUtils {
    /**
     * 构造Redis的键
     * @param template
     * @param args
     * @return
     */
    public static String buildKey(String template, Object...args){
        return String.format(template,args);
    }

    /**
     * 存数据
     * @param redisTemplate
     * @param key
     * @param value
     */
    public static void set(StringRedisTemplate redisTemplate, String key, String value){
        redisTemplate.opsForValue().set(key,value, SysConstant.TOKEN_TIMEOUT_PARAM, TimeUnit.HOURS);
    }
    /**
     * 存数据
     * @param redisTemplate
     * @param key
     * @param value
     */
    public static void setByMinutes(StringRedisTemplate redisTemplate, String key, String value,Integer time){
        redisTemplate.opsForValue().set(key,value, time, TimeUnit.MINUTES);
    }

    /**
     * 删数据
     * @param redisTemplate
     * @param key
     */
    public static void del(StringRedisTemplate redisTemplate, String key){
        redisTemplate.delete(key);
    }

    /**
     * 取数据
     * @param redisTemplate
     * @param key
     * @return
     */
    public static String get(StringRedisTemplate redisTemplate, String key){
        return redisTemplate.opsForValue().get(key);
    }
}
