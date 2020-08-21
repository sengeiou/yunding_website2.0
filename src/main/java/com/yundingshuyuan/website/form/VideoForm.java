package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class VideoForm {
    private String id;
    /**
     * 类型
     */
    private String type;

    private String name;

    private String introduce;

    /**
     * 封面图url
     */
    private String picUrl;

    /**
     * 视频url
     */
    private String videoUrl;

    private BelongForm[] belongForms;

    private Integer top;
}
