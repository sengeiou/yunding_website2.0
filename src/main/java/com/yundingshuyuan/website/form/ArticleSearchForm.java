package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class ArticleSearchForm {
    Integer pageNum;
    Integer pageSize;
    String status;
    String searchWord;
    //String orderBy;
    String type;

}
