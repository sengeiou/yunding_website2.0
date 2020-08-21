package com.yundingshuyuan.website.enums;

public enum ArticleTypeEnum {
    ARTICLE_TYPE_ENUM1(1,"思客论道"),
    ARTICLE_TYPE_ENUM2(2,"极客之路"),
    ARTICLE_TYPE_ENUM3(3,"创客空间"),
    ARTICLE_TYPE_ENUM4(4,"新闻时讯"),
    ARTICLE_TYPE_ENUM5(5,"媒体报道"),
    ARTICLE_TYPE_ENUM6(6,"通知公告"),
    ARTICLE_TYPE_ENUM7(7,"学员心得"),
    ARTICLE_TYPE_ENUM8(8,"首页轮播"),
    ;
    Integer id;
    String name;

    ArticleTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
