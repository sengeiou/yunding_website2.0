/**
 * FileName:RedisRepositoryImpl
 * Author:leeyf
 * Date:19-1-20 下午7:06
 * Description:redis仓库实体类
 */
package com.yundingshuyuan.website.repository.redis.Impl;

import com.yundingshuyuan.website.repository.redis.IRedisRepository;
import com.yundingshuyuan.website.util.redisUtils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.yundingshuyuan.website.util.redisUtils.RedisKeyTemplate.T_ACCESS_TOKEN;
import static com.yundingshuyuan.website.util.redisUtils.RedisKeyTemplate.T_USER_TOKEN;
import static com.yundingshuyuan.website.util.redisUtils.RedisUtils.buildKey;


@Component
public class RedisRepositoryImpl implements IRedisRepository {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String findAccessTokenByUserId(String userId) {
        return RedisUtils.get(redisTemplate,buildKey(T_USER_TOKEN,userId));
    }

    @Override
    public void deleteAccessToken(String accessToken) {
        RedisUtils.del(redisTemplate,buildKey(T_ACCESS_TOKEN,accessToken));
    }

    @Override
    public void saveUserAccessToken(String userId, String accessToken) {
        RedisUtils.set(redisTemplate,buildKey(T_USER_TOKEN,userId),accessToken);
    }

    @Override
    public void saveAccessToken(String userId, String accessToken) {
        RedisUtils.set(redisTemplate,buildKey(T_ACCESS_TOKEN,accessToken),userId);
    }

    @Override
    public String findUserIdByAccessToken(String accessToken) {
        return RedisUtils.get(redisTemplate,buildKey(T_ACCESS_TOKEN,accessToken));
    }
}
