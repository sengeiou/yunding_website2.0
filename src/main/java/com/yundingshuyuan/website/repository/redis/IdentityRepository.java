package com.yundingshuyuan.website.repository.redis;

import com.yundingshuyuan.website.pojo.Role;

/**
 * @author:leeyf
 * @create: 2019-07-29 16:07
 * @Description:
 */
public interface IdentityRepository {
    /**
     * 存身份信息
     */
    void saveIdentity(String userId, Role[] role);

    /**
     * 获取身份信息
     */
    Role[] findIdentityByUserId(String userId);
}
