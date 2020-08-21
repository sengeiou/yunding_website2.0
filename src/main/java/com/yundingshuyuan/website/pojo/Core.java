package com.yundingshuyuan.website.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Core implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId
    private String userId;

    private Integer core1T;

    private Integer core2J;

    private Integer core3G;

    private Integer core4X;

    private Integer core5R;

    private Integer core6S;

    /**
     * 平均分
     */
    private Integer average;

    /**
     * 期数
     */
    private Integer time;

    public Core() {

    }

    public Core(String userId, Integer core1T, Integer core2J, Integer core3G, Integer core4X, Integer core5R, Integer core6S, Integer average, Integer time) {
        this.userId = userId;
        this.core1T = core1T;
        this.core2J = core2J;
        this.core3G = core3G;
        this.core4X = core4X;
        this.core5R = core5R;
        this.core6S = core6S;
        this.average = average;
        this.time = time;
    }
}
