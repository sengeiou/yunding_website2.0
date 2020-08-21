package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class ContributionLogForm {

    /**
     * 被投票用户id
     */
    private String userId;

    /**
     * 被投票用户名称
     */
    private String userName;

    /**
     * 投票用户id
     */
    private String respUserId;

    /**
     * 投票用户名称
     */
    private String respUserName;

    /**
     *
     投票原因
     */
    private String reason;

    /**
     * 分数
     */
    private Integer score;

}
