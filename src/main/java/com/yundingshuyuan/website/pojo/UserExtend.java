package com.yundingshuyuan.website.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String userId;

    private Long popularity;

    private Integer workSize;

    private Integer articleSize;

    private Integer tipSize;

    private Integer supportSize;

    private Integer commentSize;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    public UserExtend() {

    }

    public UserExtend(String userId) {
        this.userId = userId;
    }

    public UserExtend(String userId, Long popularity, Integer workSize, Integer articleSize, Integer tipSize, Integer supportSize, Integer commentSize, LocalDateTime createdAt) {
        this.userId = userId;
        this.popularity = popularity;
        this.workSize = workSize;
        this.articleSize = articleSize;
        this.tipSize = tipSize;
        this.supportSize = supportSize;
        this.commentSize = commentSize;
        this.createdAt = createdAt;
    }
}
