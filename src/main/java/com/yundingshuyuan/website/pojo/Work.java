package com.yundingshuyuan.website.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Work implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 作品点赞
     */
    private Integer support;

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

    /**
     * 创始时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    /**
     * 热度
     */
    private Integer hot;

    /**
     * 期数
     */
    private Integer time;

    /**
     * 置顶
     */
    private Integer top;

    /**
     * 浏览量
     */
    private Integer views;


}
