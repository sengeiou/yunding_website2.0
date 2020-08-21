package com.yundingshuyuan.website.enums;

public enum RightEnum {
    RIGHT_ENUM1(1,"登录"),
    RIGHT_ENUM2(2,"评论"),
    RIGHT_ENUM3(3,"点赞"),
    RIGHT_ENUM4(4,"文章编辑"),
    RIGHT_ENUM5(5,"授权"),
    RIGHT_ENUM6(6,"产品编辑"),
    RIGHT_ENUM7(7,"视频编辑"),
    RIGHT_ENUM8(8,"相册编辑"),
    RIGHT_ENUM9(9,"后台登录"),
    ;
    Integer id;
    String RightName;

    RightEnum(Integer id, String rightName) {
        this.id = id;
        RightName = rightName;
    }

    public Integer getId() {
        return id;
    }

    public String getRightName() {
        return RightName;
    }
}
