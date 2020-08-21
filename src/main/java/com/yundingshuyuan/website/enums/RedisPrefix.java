package com.yundingshuyuan.website.enums;

import java.util.concurrent.TimeUnit;

public enum  RedisPrefix {
    // ===============  redis Prefix ====================
    /**
     * 用户token 前缀及有效期
     */
    USER_TOKEN("user_token:%s", 24, TimeUnit.HOURS, "CURRENT_USER_TIME_OUT"),
    ACCESS_TOKEN("token:%s", 24, TimeUnit.HOURS, "CURRENT_USER_TIME_OUT"),
    /**
     * 注册验证码
     */
    VERIFICATION_CODE("verification:%s",10,TimeUnit.MINUTES,"CURRENT_USER_TIME_OUT"),

    /**
     * 用户身份
     */
    USER_IDENTITY("user_identity:%s",24,TimeUnit.HOURS,"IDENTITY_TIME_OUT"),
    /**
     * article文章缓存
     */
    ATRICLE("article:",15,TimeUnit.MINUTES,"ATRICLE_TIME_OUT"),
    /**
     * 打分记录
     */
    CONTRIBUTION_LOG("contribution_log:",-1,null,"CONTRIBUTION_LOG_TIME_OUT"),
    SESSION_ID("session_id:",24 ,TimeUnit.HOURS ,"SESSION_ID_TIME_OUT" ),
    IP("ip_host:",24 ,TimeUnit.HOURS ,"IP_ID_TIME_OUT" );

    /**
     * 前缀
     */
    private String prefix;
    /**
     * 过期时间 具体时间以下面时间单位为准
     */
    private int expire;
    /**
     * 时间单位
     */
    private TimeUnit timeUnit;
    /**
     * 描述
     */
    private String msg;

    RedisPrefix(String prefix, int expire, TimeUnit timeUnit, String msg) {
        this.prefix = prefix;
        this.expire = expire;
        this.timeUnit = timeUnit;
        this.msg = msg;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}