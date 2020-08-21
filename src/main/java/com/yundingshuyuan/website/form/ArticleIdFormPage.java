package com.yundingshuyuan.website.form;


import lombok.Data;

@Data
public class ArticleIdFormPage {

    String userId;

    String articleId;

    int pageSize;

    int pageNum;

    String type;

}
