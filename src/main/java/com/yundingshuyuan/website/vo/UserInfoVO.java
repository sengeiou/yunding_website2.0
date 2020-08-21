package com.yundingshuyuan.website.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yundingshuyuan.website.pojo.Core;
import com.yundingshuyuan.website.pojo.Role;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author:leeyf
 * @create: 2019-04-02 20:18
 * @Description:
 */
@Data
public class UserInfoVO {
    private String id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String image;
    /**
     * 用户封面
     */
    private String cover;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 方向：java、js、前端、python、机电、设计、秘书处
     */
    private String direction;

    /**
     * 部门：云顶院（5）、创客团（4）、极客团（3）、工程师（2）、学员（1）
     */
    private String department;

    private Role[] roles;

    /**
     * 等级：1、2、3
     */
    private String level;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private String sex;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String professional;

    /**
     * 入学时间
     */
    private Integer admissionTime;
    /**
     * 云龄
     */
    private Integer yunOld;

    /**
     * 个性签名
     */
    private String signature;
    private Integer score;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    /**
     * core
     */
    private Core core;

}