package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class VideoPageForm {
    private String id;
    private String type;
    private  Integer top;
    private Boolean order;
    private String orderBy;

    Integer pageNum;
    Integer pageSize;

}
