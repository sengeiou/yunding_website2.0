package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class ListArticleForm {
    String id;

    String label;

    String userId;

    String status;

    Integer top;

    String orderBy;

    Boolean order;

    Integer pageSize = 10;
    Integer pageNum = 0;
}
