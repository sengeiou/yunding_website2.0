/**
 * FileName:RedisKeyTemplate
 * Author:leeyf
 * Date:19-1-20 下午7:10
 * Description:redis键,模板
 */
package com.yundingshuyuan.website.util.redisUtils;

public class RedisKeyTemplate {
    /**
     * key-存储token 信息
     */
    public final static String T_ACCESS_TOKEN = "TOKEN:%s";

    /**
     * key-存储用户和token的对应关系
     */
    public final static String T_USER_TOKEN = "USER_TOKEN:%s";


}
