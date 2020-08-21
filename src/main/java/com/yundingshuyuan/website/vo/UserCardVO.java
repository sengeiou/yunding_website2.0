package com.yundingshuyuan.website.vo;

import lombok.Data;

@Data
public class UserCardVO {
    String userId;

    String image;

    String name;

    String nickname;

    String direction;

    String department;

    String sex;

    String signature;

    Integer yunOld;

    String college;

    String professional;

    Long popularity;

    Integer workSize;

    Integer articleSize;

    Integer tipSize;

    /**
     * 获取荣誉期数
     */
    String[] contributions;


}
