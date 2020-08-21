package com.yundingshuyuan.website.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author:leeyf
 * @create: 2019-03-13 20:23
 * @Description:
 */
@Data
public class UserNotificationVO {
    private Integer id;

    /**
     * 1:评论 2：私信 3:喜欢和赞 4:关注 5:其它提醒
     */
    private Integer type;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 当前用户id
     */
    private String userId;

    /**
     * 目标用户id
     */
    private String targetUserId;

    /**
     * 已读:1未读:0
     */
    private Integer status;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private String name;

    private String avatar;
}