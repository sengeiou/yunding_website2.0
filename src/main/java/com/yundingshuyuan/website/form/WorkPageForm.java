package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class WorkPageForm {
    /**
     * 作品id
     */
    private String id;

    /**
     * 类型
     */
    private String type;
    /**
     * 期数
     */
    private Integer time;
    private Integer top;

    Boolean order;
    String orderBy;
    Integer pageNum;
    Integer pageSize;

}
