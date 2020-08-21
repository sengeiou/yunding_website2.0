package com.yundingshuyuan.website.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String image;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 方向：java、js、前端、python、机电、设计、秘书处
     */
    private String direction;

    /**
     * 部门：云顶院（5）、创客团（4）、极客团（3）、工程师（2）、学员（1）默认为0账号不可用
     */
    private String department;

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
     * 个性签名
     */
    private String signature;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    /**
     * 用户封面
     */
    private String cover;
    /**
     * 当前总分值
     */
    private Integer score;

    /**
     * 获取荣誉期数
     */
    private String contribution;


    public User(String s) {
        this.department =s;
    }


    public User() {

    }

    public User(int calculationCore) {
        this.score = calculationCore;
    }
}
