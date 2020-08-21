package com.yundingshuyuan.website.form;

import com.yundingshuyuan.website.enums.ArticleStatusEnum;
import lombok.Data;

@Data
public class ArticleForm {

    String id;
    /**
     * 标题
     */
    private String title;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 标签：大家谈、技术流、思想流
     */
    private String label;
    /**
     * 封面图
     */
    private String cover;

    /**
     * 内容
     */
    private String content;
    /**
     * 简介
     */
    private String introduce;
    /**
     * 状态
     */
    private ArticleStatusEnum status;

    /**
     * 作者
     */
    private String author;

    /**
     * 置顶：1
     */
    private Integer top;

    private Integer sort;
}
