package com.yundingshuyuan.website.enums;

import lombok.Getter;

/**
 * @author: leeyf
 * @create: 2019-01-29 17:54
 * @Description: 错误类型
 */
@Getter
public enum ErrorCodeEnum {
    /**
     * 参数错误
     */
    PARAM_ERROR(1001,"上传参数错误"),
    /**
     * 验证码错误
     */
    CODE_ERROR(1002,"验证码获取错误"),
    PHONE_CODE_ERROR(1003,"手机验证码错误"),
    USER_EXISTS(1004,"用户已存在"),
    PASSWORD_ERROR(1005,"用户名或密码错误"),
    PASSWORD_MORE_ERROR(1008,"密码错误次数太多"),
    USERNAME_ERROR(1006,"用户名错误"),
    USER_NO_ERROR(1006,"用户不存在"),
    JSON_TRANS_ERROR(1007,"JSON转化错误"),
    USER_IDENTITY_ERROR(2001,"用户权限错误"),

    ERROR_TOKEN(401,"token错误"),
    USER_ERROR(1009,"用户错误"),
    USER_ROLE_ERROR(1010,"用户角色错误"),
    USER_ENSHRINE_ERROR(1101,"添加个人收藏错误"),
    USER_ENSHRINE_EXISTS(1102,"个人收藏已存在"),
    USER_FANS_EXISTS(1202,"已经关注过了"),
    USER_PHONE_USED(501,"该手机号已注册"),
    USER_EMAIL_USED(501,"该邮箱已注册"),
    UK_KNOW_ERROR(500,"未知异常"),
    IMAGE_UPLOAD_ERROR(1103,"图片上传错误"),
    IDENTITY_UN_ADMIN(555,"用户权限不足"),
    IDENTITY_ERROR(1009,"User身份错误(未激活)"),
    ARTICLE_UNEXIST(3001,"文章id无效"),
    ARTICLE_SUPPORTED(3002,"已经点赞"),
    UPLOAD_NOT_EMPTY(4001,"上传文件不能为空"),
    BELONG_EXIST(3003,"归属关系已存在"),
    NOT_SCORE(3008,"打分已进入核算阶段,禁止打分"),
    RESPUSERNAME_EMPTY(3009,"请去个人中心填写真实姓名")
    ;
    ErrorCodeEnum(Integer code, String msg){
        this.code =code;
        this.msg = msg;
    }

    Integer code;

    String msg;

    public <T> T getMsg() {
        return (T) msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}