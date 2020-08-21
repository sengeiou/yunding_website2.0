package com.yundingshuyuan.website.repository.redis;

public interface IRedisRepository {
    /**
     * 根据userID查询token
     * @param userId
     * @return
     */
    String findAccessTokenByUserId(String userId);

    /**
     * 删除accessToken
     * @param accessToken
     */
    void deleteAccessToken(String accessToken);

    /**
     * 保存用户ID和token的对应关系
     * @param userId
     * @param accessToken
     */
    void saveUserAccessToken(String userId, String accessToken);

    /**
     * 保存token
      * @param userId
     * @param accessToken
     */
    void saveAccessToken(String userId, String accessToken);

    /**
     * 根据token查询用户ID
     * @param accessToken
     * @return
     */
    String findUserIdByAccessToken(String accessToken);

}
