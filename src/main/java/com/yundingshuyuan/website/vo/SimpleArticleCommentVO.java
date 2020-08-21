package com.yundingshuyuan.website.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SimpleArticleCommentVO<T,S> {

    private String id;

    private String articleId;

    private String UserId;

    private String content;

    /**
     * 回复个数
     */
    private Integer replyNum;

    /**
     * 赞成数
     */
    private Integer supportCount;

    private Integer status;

    private Integer floor;

    private boolean hasSupport;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;

    private SimpleUserVO userInfo;

    private List<S> topCommentList;

}
