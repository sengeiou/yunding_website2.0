package com.yundingshuyuan.website.enums;

public enum ArticleStatusEnum {
    FINISH("FINISH"),//展示
    READY("READY"),//上传
    DRAFT("DRAFT")//草稿箱
    ;
    String name;

    ArticleStatusEnum(String name) {
        this.name = name;
    }

}
