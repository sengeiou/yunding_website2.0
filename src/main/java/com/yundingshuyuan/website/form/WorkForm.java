package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class WorkForm {
    /**
     * 作品id
     */
    private String id;

    /**
     * 作品标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;


    /**
     * 封面图
     */
    private String cover;

    /**
     * 内容
     */
    private String content;

    /**
     * 1.发表 2.删除
     */
    private Integer status;
    private Integer top;

    /**
     * 期数
     */
    private Integer time;

    private BelongForm[] belongForms;
}
