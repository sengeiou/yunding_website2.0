package com.yundingshuyuan.website.vo;

import lombok.Data;

@Data
public class SimpleUserVO {

    private String id;

    private String phone;

    private String email;


    /**
     * 昵称
     */
    private String name;

    /**
     * 签名
     */
    private String signature;

    /**
     * 头像,最大64K
     */
    private String image;

    /**
     * 性别
     */
    private String sex;

    /**
     * 工作/职位
     */
    private String job;

    /**
     * 地区 省/市
     */
    private String address;

}
