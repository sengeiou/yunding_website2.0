package com.yundingshuyuan.website.repository.redis.Impl;

import com.yundingshuyuan.website.enums.RedisPrefix;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.repository.redis.IdentityRepository;
import com.yundingshuyuan.website.util.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author:leeyf
 * @create: 2019-07-29 16:10
 * @Description:
 */
@Component
public class IdentityRepositoryImpl implements IdentityRepository {
    @Autowired
    RedisManager redisManager;
    /**
     * 存身份信息
     *
     * @param userId
     * @param roles
     */
    @Override
    public void saveIdentity(String userId, Role[] roles) {
        redisManager.setValue(RedisPrefix.USER_IDENTITY,userId,roles);
    }

    /**
     * 获取身份信息
     *
     * @param userId
     */
    @Override
    public Role[] findIdentityByUserId(String userId) {
        return redisManager.getValue(RedisPrefix.USER_IDENTITY,userId);
    }
}