package com.yundingshuyuan.website.queryTo;

import java.time.LocalDateTime;

/**
 * @author super hui
 */
public class ArticleQueryTo {

    private String id;

    private String authorId;

    private String title;

    private String label;


    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 点赞数
     */
    private Integer like;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 是否点赞
     */
    private boolean hasSupport;
}
