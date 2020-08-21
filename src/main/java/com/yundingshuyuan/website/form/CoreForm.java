package com.yundingshuyuan.website.form;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CoreForm {
    private String userId;

    @Size(max = 100)
    private Integer core1T;
    @Size(max = 100)
    private Integer core2J;
    @Size(max = 100)
    private Integer core3G;
    @Size(max = 100)
    private Integer core4X;
    @Size(max = 100)
    private Integer core5R;
    @Size(max = 100)
    private Integer core6S;
    /**
     * 期数
     */
    private Integer time;
}
