package com.yundingshuyuan.website.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleReplyVO<T,S> {

    private String id;

    private String commentId;

    private String replyCommentId;

    /**
     * 回复对象id
     */
    private String respUserId;

    /**
     * 本人id
     */
    private String userId;

    /**
     * 评论回复内容
     */
    private String content;

    private Integer status;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime createTime;

    private SimpleUserVO userInfo;

    private SimpleUserVO respUserInfo;

}
