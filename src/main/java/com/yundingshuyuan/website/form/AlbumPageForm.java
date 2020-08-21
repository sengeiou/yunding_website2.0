package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class AlbumPageForm {
    private String id;

    private String status;
    private Integer top;
    Boolean order;
    String orderBy;
    Integer pageNum;
    Integer pageSize;


}
