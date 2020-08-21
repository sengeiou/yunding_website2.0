package com.yundingshuyuan.website.constants;

/**
 * @author:leeyf
 * @create: 2019-03-25 19:32
 * @Description:
 */
public class UserConstant {
    /*redis前缀,短信发送次数*/
    public static final String SEND_PHONE_TIME = "SMS:%s";
    /*图片服务器前缀*/
    public static final String ImageRealPath = "http://39.104.201.80/yunding/";
    /*用户默认头像*/
    public static final String USER_IMAGE="https://yundingweb.oss-cn-beijing.aliyuncs.com/yunding/20191015/a903db2af93543c09371f86c569783ea-image";
    /*用户默认封面*/
    public static final String USER_COVER="https://yundingshuyuan.com/yunding/M00/00/0C/rBjuW108ShyAWwIIAACjGhPMjZs065.png";

    /*redis前缀,用户登录次数*/
    public static final String USER_LONGIN_TIME="user:%s";
    /*用户-默认个签*/
    public static final String SIGNATURE = "这个人很低调,什么都没说!";
    public static final String USER_SESSION_NAME = "UserID";
    /*用户等级*/
    public static final Integer IDENTITY_DETAIL = 5;

}