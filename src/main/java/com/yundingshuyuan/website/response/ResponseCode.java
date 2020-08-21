package com.yundingshuyuan.website.response;

/**
 * 响应码 枚举类
 * Created by Fant.J.
 */
public enum  ResponseCode {

    SUCCESS(200,"SUCCESS"),
    ERROR(500,"ERROR"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"),
    NEED_LOGIN(403,"NEED_LOGIN"),
    NULL_ERROR(404,"未找到合适的文章"),
    ACCESS_ERROR(405,"你无权限操作此文章"),

    LOGIN_ERROR(406,"你还未登陆"),
    TAG_ERROR(407,"文章的标签应该为1-3个"),
    TAG_REP_ERROR(408,"该标签已经存在，不用重复创建");
    private final int code;
    private final String desc;


    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }

}
